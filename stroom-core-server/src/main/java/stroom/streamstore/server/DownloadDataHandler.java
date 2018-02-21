/*
 * Copyright 2016 Crown Copyright
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
 */

package stroom.streamstore.server;

import stroom.entity.server.util.EntityServiceExceptionUtil;
import stroom.logging.StreamEventLog;
import stroom.security.Secured;
import stroom.servlet.SessionResourceStore;
import stroom.streamstore.shared.DownloadDataAction;
import stroom.streamstore.shared.Stream;
import stroom.task.server.AbstractTaskHandler;
import stroom.task.server.TaskHandlerBean;
import stroom.task.server.TaskManager;
import stroom.util.shared.ResourceGeneration;
import stroom.util.shared.ResourceKey;

import javax.inject.Inject;
import java.nio.file.Path;
import java.util.ArrayList;

@TaskHandlerBean(task = DownloadDataAction.class)
@Secured(Stream.EXPORT_DATA_PERMISSION)
class DownloadDataHandler extends AbstractTaskHandler<DownloadDataAction, ResourceGeneration> {
    private final SessionResourceStore sessionResourceStore;
    private final TaskManager taskManager;
    private final StreamEventLog streamEventLog;

    @Inject
    DownloadDataHandler(final SessionResourceStore sessionResourceStore,
                        final TaskManager taskManager,
                        final StreamEventLog streamEventLog) {
        this.sessionResourceStore = sessionResourceStore;
        this.taskManager = taskManager;
        this.streamEventLog = streamEventLog;
    }

    @Override
    public ResourceGeneration exec(final DownloadDataAction action) {
        ResourceKey resourceKey;
        try {
            // Import file.
            resourceKey = sessionResourceStore.createTempFile("StroomData.zip");
            final Path file = sessionResourceStore.getTempFile(resourceKey);

            final StreamDownloadSettings settings = new StreamDownloadSettings();
            taskManager.exec(new StreamDownloadTask(action.getUserToken(), action.getCriteria(), file, settings));

            streamEventLog.exportStream(action.getCriteria(), null);

        } catch (final Exception ex) {
            streamEventLog.exportStream(action.getCriteria(), ex);
            throw EntityServiceExceptionUtil.create(ex);
        }
        return new ResourceGeneration(resourceKey, new ArrayList<>());
    }
}
