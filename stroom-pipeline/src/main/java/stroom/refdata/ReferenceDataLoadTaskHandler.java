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

package stroom.refdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stroom.feed.server.FeedService;
import stroom.feed.shared.Feed;
import stroom.io.StreamCloser;
import stroom.pipeline.server.EncodingSelection;
import stroom.pipeline.server.LocationFactoryProxy;
import stroom.pipeline.server.PipelineService;
import stroom.pipeline.server.StreamLocationFactory;
import stroom.pipeline.server.errorhandler.ErrorReceiverIdDecorator;
import stroom.pipeline.server.errorhandler.ErrorReceiverProxy;
import stroom.pipeline.server.errorhandler.StoredErrorReceiver;
import stroom.pipeline.server.factory.Pipeline;
import stroom.pipeline.server.factory.PipelineDataCache;
import stroom.pipeline.server.factory.PipelineFactory;
import stroom.pipeline.shared.PipelineEntity;
import stroom.pipeline.shared.data.PipelineData;
import stroom.pipeline.state.FeedHolder;
import stroom.pipeline.state.PipelineHolder;
import stroom.pipeline.state.StreamHolder;
import stroom.streamstore.server.StreamSource;
import stroom.streamstore.server.StreamStore;
import stroom.streamstore.server.fs.serializable.StreamSourceInputStream;
import stroom.streamstore.server.fs.serializable.StreamSourceInputStreamProvider;
import stroom.streamstore.shared.Stream;
import stroom.streamstore.shared.StreamType;
import stroom.task.server.AbstractTaskHandler;
import stroom.task.server.TaskHandlerBean;
import stroom.util.shared.Severity;
import stroom.util.task.TaskMonitor;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;

/**
 * Processes reference data that meets some supplied criteria (feed names,
 * effective from and to dates). The process puts the reference data into key,
 * value maps that can be used later on by the FunctionFilter to perform
 * substitutions when processing events data.
 */
@TaskHandlerBean(task = ReferenceDataLoadTask.class)
class ReferenceDataLoadTaskHandler extends AbstractTaskHandler<ReferenceDataLoadTask, MapStore> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReferenceDataLoadTaskHandler.class);

    private final StreamStore streamStore;
    private final PipelineFactory pipelineFactory;
    private final MapStoreHolder mapStoreHolder;
    private final FeedService feedService;
    private final PipelineService pipelineService;
    private final PipelineHolder pipelineHolder;
    private final FeedHolder feedHolder;
    private final StreamHolder streamHolder;
    private final LocationFactoryProxy locationFactory;
    private final StreamCloser streamCloser;
    private final ErrorReceiverProxy errorReceiverProxy;
    private final TaskMonitor taskMonitor;
    private final PipelineDataCache pipelineDataCache;

    private ErrorReceiverIdDecorator errorReceiver;

    @Inject
    ReferenceDataLoadTaskHandler(final StreamStore streamStore,
                                 final PipelineFactory pipelineFactory,
                                 final MapStoreHolder mapStoreHolder,
                                 @Named("cachedFeedService") final FeedService feedService,
                                 @Named("cachedPipelineService") final PipelineService pipelineService,
                                 final PipelineHolder pipelineHolder,
                                 final FeedHolder feedHolder,
                                 final StreamHolder streamHolder,
                                 final LocationFactoryProxy locationFactory,
                                 final StreamCloser streamCloser,
                                 final ErrorReceiverProxy errorReceiverProxy,
                                 final TaskMonitor taskMonitor,
                                 final PipelineDataCache pipelineDataCache) {
        this.streamStore = streamStore;
        this.pipelineFactory = pipelineFactory;
        this.mapStoreHolder = mapStoreHolder;
        this.feedService = feedService;
        this.pipelineService = pipelineService;
        this.pipelineHolder = pipelineHolder;
        this.feedHolder = feedHolder;
        this.streamHolder = streamHolder;
        this.locationFactory = locationFactory;
        this.streamCloser = streamCloser;
        this.errorReceiverProxy = errorReceiverProxy;
        this.taskMonitor = taskMonitor;
        this.pipelineDataCache = pipelineDataCache;
    }

    /**
     * Loads reference data that meets the supplied criteria into the current
     * reference data key, value maps.
     */
    @Override
    public MapStore exec(final ReferenceDataLoadTask task) {
        final StoredErrorReceiver storedErrorReceiver = new StoredErrorReceiver();
        final MapStoreBuilder mapStoreBuilder = new MapStoreBuilderImpl(storedErrorReceiver);
        errorReceiver = new ErrorReceiverIdDecorator(getClass().getSimpleName(), storedErrorReceiver);
        errorReceiverProxy.setErrorReceiver(errorReceiver);

        try {
            final MapStoreCacheKey mapStorePoolKey = task.getMapStorePoolKey();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Loading reference data: " + mapStorePoolKey.toString());
            }

            // Open the stream source.
            final StreamSource streamSource = streamStore.openStreamSource(mapStorePoolKey.getStreamId());
            if (streamSource != null) {
                final Stream stream = streamSource.getStream();
                try {
                    // Load the feed.
                    final Feed feed = feedService.load(stream.getFeed());
                    feedHolder.setFeed(feed);

                    // Set the pipeline so it can be used by a filter if needed.
                    final PipelineEntity pipelineEntity = pipelineService
                            .loadByUuid(mapStorePoolKey.getPipeline().getUuid());
                    pipelineHolder.setPipeline(pipelineEntity);

                    // Create the parser.
                    final PipelineData pipelineData = pipelineDataCache.get(pipelineEntity);
                    final Pipeline pipeline = pipelineFactory.create(pipelineData);

                    populateMaps(pipeline, stream, streamSource, feed, stream.getStreamType(), mapStoreBuilder);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Finished loading reference data: " + mapStorePoolKey.toString());
                    }
                } finally {
                    try {
                        // Close all open streams.
                        streamCloser.close();
                    } catch (final IOException e) {
                        log(Severity.FATAL_ERROR, e.getMessage(), e);
                    }

                    streamStore.closeStreamSource(streamSource);
                }
            }
        } catch (final Exception e) {
            log(Severity.FATAL_ERROR, e.getMessage(), e);
        }

        return mapStoreBuilder.getMapStore();
    }

    private void populateMaps(final Pipeline pipeline, final Stream stream, final StreamSource streamSource,
                              final Feed feed, final StreamType streamType, final MapStoreBuilder mapStoreBuilder) {
        try {
            // Get the stream providers.
            streamHolder.setStream(stream);
            streamHolder.addProvider(streamSource);
            streamHolder.addProvider(streamSource.getChildStream(StreamType.META));
            streamHolder.addProvider(streamSource.getChildStream(StreamType.CONTEXT));

            // Get the main stream provider.
            final StreamSourceInputStreamProvider mainProvider = streamHolder.getProvider(streamSource.getType());

            // Set the map store.
            mapStoreHolder.setMapStoreBuilder(mapStoreBuilder);

            // Start processing.
            try {
                pipeline.startProcessing();
            } catch (final Exception e) {
                // An exception during start processing is definitely a failure.
                log(Severity.FATAL_ERROR, e.getMessage(), e);
            }

            try {
                // Get the appropriate encoding for the stream type.
                final String encoding = EncodingSelection.select(feed, streamType);

                final StreamLocationFactory streamLocationFactory = new StreamLocationFactory();
                locationFactory.setLocationFactory(streamLocationFactory);

                // Loop over the stream boundaries and process each
                // sequentially.
                final long streamCount = mainProvider.getStreamCount();
                for (long streamNo = 0; streamNo < streamCount && !taskMonitor.isTerminated(); streamNo++) {
                    streamHolder.setStreamNo(streamNo);
                    streamLocationFactory.setStreamNo(streamNo + 1);

                    // Get the stream.
                    final StreamSourceInputStream inputStream = mainProvider.getStream(streamNo);

                    // Process the boundary.
                    try {
                        pipeline.process(inputStream, encoding);
                    } catch (final Exception e) {
                        log(Severity.FATAL_ERROR, e.getMessage(), e);
                    }
                }
            } catch (final Exception e) {
                log(Severity.FATAL_ERROR, e.getMessage(), e);
            } finally {
                try {
                    pipeline.endProcessing();
                } catch (final Exception e) {
                    log(Severity.FATAL_ERROR, e.getMessage(), e);
                }
            }

        } catch (final Exception e) {
            log(Severity.FATAL_ERROR, e.getMessage(), e);
        }
    }

    private void log(final Severity severity, final String message, final Throwable e) {
        LOGGER.trace(message, e);

        String msg = message;
        if (msg == null) {
            msg = e.toString();
        }
        errorReceiver.log(severity, null, getClass().getSimpleName(), msg, e);
    }
}
