/*
 * Copyright 2017 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package stroom.data.store.impl.fs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stroom.node.NodeInfo;
import stroom.node.VolumeService;
import stroom.node.shared.FindVolumeCriteria;
import stroom.node.shared.VolumeEntity;
import stroom.task.AsyncTaskHelper;
import stroom.task.TaskCallbackAdaptor;
import stroom.task.api.TaskManager;
import stroom.task.api.TaskContext;
import stroom.task.shared.Task;
import stroom.util.io.CloseableUtil;
import stroom.util.io.StreamUtil;
import stroom.util.logging.LogExecutionTime;
import stroom.util.shared.ModelStringUtil;
import stroom.util.shared.VoidResult;

import javax.inject.Inject;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Task to clean the stream store.
 */
class FileSystemCleanExecutor {
    private static final String DELETE_OUT = "delete.out";

    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemCleanExecutor.class);
    private final VolumeService volumeService;
    private final TaskContext taskContext;
    private final TaskManager taskManager;
    private final NodeInfo nodeInfo;
    private final int batchSize;
    private final long oldAge;
    private final boolean deleteOut;

    private AsyncTaskHelper<VoidResult> asyncTaskHelper;

    @Inject
    FileSystemCleanExecutor(final VolumeService volumeService,
                            final TaskContext taskContext,
                            final TaskManager taskManager,
                            final NodeInfo nodeInfo,
                            final DataStoreServiceConfig config) {
        this.volumeService = volumeService;
        this.taskContext = taskContext;
        this.taskManager = taskManager;
        this.nodeInfo = nodeInfo;
        this.batchSize = config.getDeleteBatchSize();

        Long age;
        try {
            age = ModelStringUtil.parseDurationString(config.getFileSystemCleanOldAge());
            if (age == null) {
                age = ModelStringUtil.parseDurationString("1d");
            }
        } catch (final NumberFormatException e) {
            LOGGER.error("Unable to parse property 'stroom.fileSystemCleanOldAge' value '" + config.getFileSystemCleanOldAge()
                    + "', using default of '1d' instead", e);
            age = ModelStringUtil.parseDurationString("1d");
        }
        this.oldAge = age;
        this.deleteOut = config.isFileSystemCleanDeleteOut();
    }

    Long getOldAge() {
        return oldAge;
    }

    public boolean isDelete() {
        return !deleteOut;
    }

    AsyncTaskHelper<VoidResult> getAsyncTaskHelper() {
        return asyncTaskHelper;
    }

    private void logInfo(final Object... args) {
        Arrays.asList(args).forEach(arg -> LOGGER.info(arg.toString()));
        taskContext.info(args);
    }

    public void exec(final Task<?> task) {
        final long nodeId = nodeInfo.getDefaultNode().getId();
        clean(task, nodeId);
    }

    public void clean(final Task<?> task, final long nodeId) {
        final Map<VolumeEntity, FileSystemCleanProgress> taskProgressMap = new HashMap<>();
        final Map<VolumeEntity, PrintWriter> printWriterMap = new HashMap<>();

        // Load the node.
        asyncTaskHelper = new AsyncTaskHelper<>(null, taskContext, taskManager, batchSize);

        logInfo("Starting file system clean task. oldAge = {}", ModelStringUtil.formatDurationString(oldAge));

        final LogExecutionTime logExecutionTime = new LogExecutionTime();

        final FindVolumeCriteria criteria = new FindVolumeCriteria();
        criteria.getNodeIdSet().add(nodeId);
        final List<VolumeEntity> volumeList = volumeService.find(criteria);

        try {
            if (volumeList != null && volumeList.size() > 0) {
                // Add to the task steps remaining.

                for (final VolumeEntity volume : volumeList) {
                    final FileSystemCleanProgress taskProgress = new FileSystemCleanProgress();
                    if (deleteOut) {
                        final Path dir = Paths.get(volume.getPath());
                        if (Files.isDirectory(dir)) {
                            try {
                                printWriterMap
                                        .put(volume,
                                                new PrintWriter(Files.newBufferedWriter(dir.resolve(DELETE_OUT), StreamUtil.DEFAULT_CHARSET)));
                            } catch (final IOException e) {
                                LOGGER.error("exec() - Error opening file", e);
                            }
                        }
                    }
                    taskProgressMap.put(volume, taskProgress);
                    final FileSystemCleanSubTask subTask = new FileSystemCleanSubTask(this, task, taskProgress, volume,
                            "", "");

                    asyncTaskHelper.fork(subTask, new TaskCallbackAdaptor<VoidResult>() {
                        @Override
                        public void onSuccess(final VoidResult result) {
                            taskProgress.addScanComplete();
                        }

                        @Override
                        public void onFailure(final Throwable t) {
                            taskProgress.addScanComplete();
                        }
                    });
                }

                while (asyncTaskHelper.busy()) {
                    try {
                        // Wait for all task steps to complete.
                        Thread.sleep(500);
                    } catch (final InterruptedException e) {
                        LOGGER.error(e.getMessage(), e);

                        // Continue to interrupt this thread.
                        Thread.currentThread().interrupt();
                    }

                    if (Thread.currentThread().isInterrupted()) {
                        logInfo("Stopping file system clean task.");
                        asyncTaskHelper.clear();
                    }

                    final StringBuilder trace = new StringBuilder();

                    for (final VolumeEntity volume : volumeList) {
                        final FileSystemCleanProgress taskProgress = taskProgressMap.get(volume);

                        trace.append(volume.getPath());
                        trace.append(" (Scan Dir/File ");
                        trace.append(taskProgress.getScanDirCount());
                        trace.append("/");
                        trace.append(taskProgress.getScanFileCount());
                        trace.append(", Del ");
                        trace.append(taskProgress.getScanDeleteCount());
                        trace.append(") ");

                        String line;
                        try {
                            final PrintWriter deletePrintWriter = printWriterMap.get(volume);
                            while ((line = taskProgress.getLineQueue().poll()) != null) {
                                if (deletePrintWriter != null) {
                                    deletePrintWriter.println(line);
                                }
                            }
                        } catch (final RuntimeException e) {
                            LOGGER.error("exec() - Error writing " + DELETE_OUT, e);
                            taskContext.terminate();
                        }
                    }
                    logInfo(trace.toString());
                }
            }
        } finally {
            printWriterMap.values().forEach(CloseableUtil::closeLogAndIgnoreException);
        }

        logInfo("start() - Completed file system clean task in {}", logExecutionTime);
    }
}
