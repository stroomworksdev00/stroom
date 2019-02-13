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

package stroom.processor;

import stroom.docref.DocRef;
import stroom.entity.shared.BaseResultList;
import stroom.processor.shared.FindStreamProcessorFilterCriteria;
import stroom.processor.shared.Processor;
import stroom.processor.shared.ProcessorFilter;
import stroom.processor.shared.QueryData;

import java.util.Optional;

public interface StreamProcessorFilterService {
//        extends BaseEntityService<ProcessorFilter>, FindService<ProcessorFilter, FindStreamProcessorFilterCriteria> {

    ProcessorFilter update(final ProcessorFilter processorFilter);
//
//    boolean delete(final int id);
//
    Optional<ProcessorFilter> fetch(final int id);

    BaseResultList<ProcessorFilter> find(FindStreamProcessorFilterCriteria criteria);

    ProcessorFilter createFilter(final Processor streamProcessor,
                                 final QueryData queryData,
                                 final boolean enabled,
                                 final int priority);

    ProcessorFilter createFilter(final DocRef pipelineRef,
                                 final QueryData queryData,
                                 final boolean enabled,
                                 final int priority);
}
