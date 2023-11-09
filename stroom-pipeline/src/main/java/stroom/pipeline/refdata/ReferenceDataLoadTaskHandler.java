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

package stroom.pipeline.refdata;

import stroom.data.store.api.InputStreamProvider;
import stroom.data.store.api.Source;
import stroom.data.store.api.Store;
import stroom.feed.api.FeedProperties;
import stroom.meta.shared.Meta;
import stroom.pipeline.LocationFactoryProxy;
import stroom.pipeline.PipelineStore;
import stroom.pipeline.StreamLocationFactory;
import stroom.pipeline.errorhandler.ErrorReceiverIdDecorator;
import stroom.pipeline.errorhandler.ErrorReceiverProxy;
import stroom.pipeline.errorhandler.LoggedException;
import stroom.pipeline.errorhandler.StoredErrorReceiver;
import stroom.pipeline.factory.Pipeline;
import stroom.pipeline.factory.PipelineDataCache;
import stroom.pipeline.factory.PipelineFactory;
import stroom.pipeline.refdata.store.ProcessingState;
import stroom.pipeline.refdata.store.RefDataStore;
import stroom.pipeline.refdata.store.RefDataStoreFactory;
import stroom.pipeline.refdata.store.RefStreamDefinition;
import stroom.pipeline.shared.PipelineDoc;
import stroom.pipeline.shared.data.PipelineData;
import stroom.pipeline.state.FeedHolder;
import stroom.pipeline.state.MetaDataHolder;
import stroom.pipeline.state.MetaHolder;
import stroom.pipeline.state.PipelineHolder;
import stroom.pipeline.task.StreamMetaDataProvider;
import stroom.security.api.SecurityContext;
import stroom.task.api.TaskContext;
import stroom.task.api.TaskTerminatedException;
import stroom.util.logging.LogUtil;
import stroom.util.shared.Severity;

import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

/**
 * Processes reference data that meets some supplied criteria (feed names,
 * effective from and to dates). The process puts the reference data into key,
 * value maps that can be used later on by the FunctionFilter to perform
 * substitutions when processing events data.
 */

class ReferenceDataLoadTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReferenceDataLoadTaskHandler.class);

    private final Store streamStore;
    private final PipelineFactory pipelineFactory;
    private final PipelineStore pipelineStore;
    private final PipelineHolder pipelineHolder;
    private final FeedHolder feedHolder;
    private final FeedProperties feedProperties;
    private final MetaDataHolder metaDataHolder;
    private final MetaHolder metaHolder;
    private final RefDataLoaderHolder refDataLoaderHolder;
    private final RefDataStoreFactory refDataStoreFactory;
    private final LocationFactoryProxy locationFactory;
    private final ErrorReceiverProxy errorReceiverProxy;
    private final PipelineDataCache pipelineDataCache;
    private final SecurityContext securityContext;

    private TaskContext taskContext;
    private ErrorReceiverIdDecorator errorReceiver;

    @Inject
    ReferenceDataLoadTaskHandler(final Store streamStore,
                                 final PipelineFactory pipelineFactory,
                                 final PipelineStore pipelineStore,
                                 final PipelineHolder pipelineHolder,
                                 final FeedHolder feedHolder,
                                 final FeedProperties feedProperties,
                                 final MetaDataHolder metaDataHolder,
                                 final MetaHolder metaHolder,
                                 final RefDataLoaderHolder refDataLoaderHolder,
                                 final RefDataStoreFactory refDataStoreFactory,
                                 final LocationFactoryProxy locationFactory,
                                 final ErrorReceiverProxy errorReceiverProxy,
                                 final PipelineDataCache pipelineDataCache,
                                 final SecurityContext securityContext) {
        this.streamStore = streamStore;
        this.pipelineFactory = pipelineFactory;
        this.pipelineStore = pipelineStore;
        this.pipelineHolder = pipelineHolder;
        this.feedHolder = feedHolder;
        this.feedProperties = feedProperties;
        this.refDataStoreFactory = refDataStoreFactory;
        this.metaDataHolder = metaDataHolder;
        this.locationFactory = locationFactory;
        this.metaHolder = metaHolder;
        this.refDataLoaderHolder = refDataLoaderHolder;
        this.errorReceiverProxy = errorReceiverProxy;
        this.pipelineDataCache = pipelineDataCache;
        this.securityContext = securityContext;
    }

    /**
     * Loads reference data that meets the supplied criteria into the current
     * reference data key, value maps.
     */
    public StoredErrorReceiver exec(final TaskContext taskContext,
                                    final RefStreamDefinition refStreamDefinition) {
        this.taskContext = taskContext;
        final StoredErrorReceiver storedErrorReceiver = new StoredErrorReceiver();
        securityContext.secure(() -> {
            // Elevate user permissions so that inherited pipelines that the user only has 'Use' permission
            // on can be read.
            securityContext.useAsRead(() -> {
                errorReceiver = new ErrorReceiverIdDecorator(getClass().getSimpleName(), storedErrorReceiver);
                errorReceiverProxy.setErrorReceiver(errorReceiver);

                LOGGER.debug("Loading reference data: {}", refStreamDefinition);
                taskContext.info(() -> "Loading " + refStreamDefinition);

                // Open the stream source.
                try (final Source source = streamStore.openSource(refStreamDefinition.getStreamId())) {
                    if (source != null) {
                        final Meta meta = source.getMeta();

                        // Load the feed.
                        final String feedName = meta.getFeedName();
                        feedHolder.setFeedName(feedName);

                        // Setup the meta data holder.
                        metaDataHolder.setMetaDataProvider(new StreamMetaDataProvider(metaHolder, pipelineStore));

                        // Set the pipeline so it can be used by a filter if needed.
                        if (refStreamDefinition.getPipelineDocRef() == null ||
                                refStreamDefinition.getPipelineDocRef().getUuid() == null) {
                            throw new RuntimeException("Null reference pipeline");
                        }
                        final PipelineDoc pipelineDoc = pipelineStore
                                .readDocument(refStreamDefinition.getPipelineDocRef());
                        if (pipelineDoc == null) {
                            throw new RuntimeException("Unable to find pipeline with UUID: " +
                                    refStreamDefinition.getPipelineDocRef().getUuid());
                        }
                        pipelineHolder.setPipeline(refStreamDefinition.getPipelineDocRef());

                        // Create the parser.
                        final PipelineData pipelineData = pipelineDataCache.get(pipelineDoc);
                        final Pipeline pipeline = pipelineFactory.create(pipelineData, taskContext);

                        populateMaps(
                                pipeline,
                                meta,
                                source,
                                feedName,
                                refStreamDefinition);

                        LOGGER.debug("Finished loading reference data: {}", refStreamDefinition);
                        taskContext.info(() -> "Finished " + refStreamDefinition);
                    }
                } catch (final Exception e) {
                    log(Severity.ERROR, e.getMessage(), e);
                }
            });
        });
        return storedErrorReceiver;
    }

    private void populateMaps(final Pipeline pipeline,
                              final Meta meta,
                              final Source source,
                              final String feedName,
                              final RefStreamDefinition refStreamDefinition) {
        // Set the source meta.
        metaHolder.setMeta(meta);

        // Get the store specific to this load
        final RefDataStore refDataStore = refDataStoreFactory.getOffHeapStore(refStreamDefinition);

        refDataStore.doWithLoaderUnlessComplete(refStreamDefinition, meta.getEffectiveMs(), refDataLoader -> {
            // we are now blocking any other thread loading the same refStreamDefinition
            // and know that this stream has not already been loaded.

            try {
                // set this loader in the holder so it is available to the pipeline filters
                refDataLoaderHolder.setRefDataLoader(refDataLoader);

                // Start processing.
                pipeline.startProcessing();

                // Get the appropriate encoding for the stream type.
                final String encoding = feedProperties.getEncoding(
                        feedName, meta.getTypeName(), null);
                LOGGER.debug("Using encoding '{}' for feed {}", encoding, feedName);

                final StreamLocationFactory streamLocationFactory = new StreamLocationFactory();
                locationFactory.setLocationFactory(streamLocationFactory);

                // Loop over the stream boundaries and process each sequentially.
                // Typically, ref data will only have a single partIndex so if there are
                // multiple then overrideExisting may be needed.
                final long count = source.count();
                for (long index = 0; index < count && !taskContext.isTerminated(); index++) {
                    metaHolder.setPartIndex(index);
                    streamLocationFactory.setPartIndex(index);

                    try (final InputStreamProvider inputStreamProvider = source.get(index)) {
                        // Get the stream providers.
                        metaHolder.setInputStreamProvider(inputStreamProvider);

                        // Get the stream.
                        final InputStream inputStream = inputStreamProvider.get();

                        // Process the boundary.
                        //process the pipeline, ref data will be loaded via the ReferenceDataFilter
                        try {
                            pipeline.process(inputStream, encoding);
                        } finally {
                            // This calls endProcessing on the ReferenceDataFilter, which will initiate
                            // the transfer from staging to ref store
                            pipeline.endProcessing();
                        }
                    }
                }

                if (taskContext.isTerminated()) {
                    log(Severity.WARNING,
                            LogUtil.message("Load terminated while loading stream {} from feed '{}'",
                                    refStreamDefinition.getStreamId(),
                                    feedName),
                            null);
                    refDataLoader.completeProcessing(ProcessingState.TERMINATED);
                } else {
                    log(Severity.INFO,
                            LogUtil.message("Successfully loaded stream {} from feed: '{}'",
                                    refStreamDefinition.getStreamId(),
                                    feedName),
                            null);
                    refDataLoader.completeProcessing(ProcessingState.COMPLETE);
                }
            } catch (TaskTerminatedException e) {
                // Task terminated
                log(Severity.FATAL_ERROR, e.getMessage(), e);
                refDataLoader.completeProcessing(ProcessingState.TERMINATED);
            } catch (UncheckedIOException | IOException e) {
                // Missing ref strm file or unable to read it
                log(Severity.FATAL_ERROR,
                        "Error reading reference stream "
                                + refStreamDefinition.getStreamId()
                                + ":"
                                + refStreamDefinition.getPartNumber()
                                + " - "
                                + e.getMessage(),
                        e);
                refDataLoader.completeProcessing(ProcessingState.FAILED);
            } catch (Exception e) {
                // Something unexpected happened
                log(Severity.ERROR, e.getMessage(), e);
                refDataLoader.completeProcessing(ProcessingState.FAILED);
            }
        });

        // clear the reference to the loader now we have finished with it
        refDataLoaderHolder.setRefDataLoader(null);
    }

    private void log(final Severity severity, final String message, final Throwable e) {
        LOGGER.trace(message, e);

        // LoggedException has already been logged
        if (errorReceiverProxy != null && !(e instanceof LoggedException)) {

            String msg = message;
            if (msg == null) {
                msg = e.toString();
            }
            errorReceiver.log(severity, null, getClass().getSimpleName(), msg, e);
        }
    }
}
