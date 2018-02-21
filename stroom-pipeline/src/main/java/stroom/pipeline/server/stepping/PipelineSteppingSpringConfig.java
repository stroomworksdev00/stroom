/*
 * Copyright 2018 Crown Copyright
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

package stroom.pipeline.server.stepping;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import stroom.feed.server.FeedService;
import stroom.io.StreamCloser;
import stroom.pipeline.server.LocationFactoryProxy;
import stroom.pipeline.server.PipelineService;
import stroom.pipeline.server.errorhandler.ErrorReceiverProxy;
import stroom.pipeline.server.factory.PipelineDataCache;
import stroom.pipeline.server.factory.PipelineFactory;
import stroom.pipeline.state.CurrentUserHolder;
import stroom.pipeline.state.FeedHolder;
import stroom.pipeline.state.PipelineContext;
import stroom.pipeline.state.PipelineHolder;
import stroom.pipeline.state.StreamHolder;
import stroom.security.SecurityContext;
import stroom.streamstore.server.StreamStore;
import stroom.streamstore.server.StreamTypeService;
import stroom.util.spring.StroomScope;
import stroom.util.task.TaskMonitor;

import javax.inject.Named;

@Configuration
public class PipelineSteppingSpringConfig {
    @Bean
    @Scope(value = StroomScope.TASK)
    public GetPipelineForStreamHandler getPipelineForStreamHandler(final StreamStore streamStore,
                                                                   final PipelineService pipelineService,
                                                                   final FeedService feedService,
                                                                   final SecurityContext securityContext) {
        return new GetPipelineForStreamHandler(streamStore, pipelineService, feedService, securityContext);
    }

    @Bean
    @Scope(value = StroomScope.TASK)
    public SteppingController steppingController(final StreamHolder streamHolder,
                                                 final LocationFactoryProxy locationFactory,
                                                 final SteppingResponseCache steppingResponseCache,
                                                 final ErrorReceiverProxy errorReceiverProxy) {
        return new SteppingController(streamHolder, locationFactory, steppingResponseCache, errorReceiverProxy);
    }

    @Bean
    @Scope(value = StroomScope.TASK)
    public SteppingResponseCache steppingResponseCache() {
        return new SteppingResponseCache();
    }

    @Bean
    @Scope(value = StroomScope.TASK)
    public SteppingTaskHandler steppingTaskHandler(final StreamStore streamStore,
                                                   final StreamCloser streamCloser,
                                                   final FeedService feedService,
                                                   @Named("cachedStreamTypeService") final StreamTypeService streamTypeService,
                                                   final TaskMonitor taskMonitor,
                                                   final FeedHolder feedHolder,
                                                   final PipelineHolder pipelineHolder,
                                                   final StreamHolder streamHolder,
                                                   final LocationFactoryProxy locationFactory,
                                                   final CurrentUserHolder currentUserHolder,
                                                   final SteppingController controller,
                                                   final PipelineService pipelineService,
                                                   final PipelineFactory pipelineFactory,
                                                   final ErrorReceiverProxy errorReceiverProxy,
                                                   final SteppingResponseCache steppingResponseCache,
                                                   final PipelineDataCache pipelineDataCache,
                                                   final PipelineContext pipelineContext,
                                                   final SecurityContext securityContext) {
        return new SteppingTaskHandler(streamStore,
                streamCloser,
                feedService,
                streamTypeService,
                taskMonitor,
                feedHolder,
                pipelineHolder,
                streamHolder,
                locationFactory,
                currentUserHolder,
                controller,
                pipelineService,
                pipelineFactory,
                errorReceiverProxy,
                steppingResponseCache,
                pipelineDataCache,
                pipelineContext,
                securityContext);
    }
}