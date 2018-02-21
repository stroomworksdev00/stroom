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

package stroom.pipeline;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import stroom.importexport.server.ImportExportHelper;
import stroom.pipeline.server.MockPipelineService;
import stroom.pipeline.server.MockTextConverterService;
import stroom.pipeline.server.MockXSLTService;
import stroom.util.spring.StroomSpringProfiles;

@Configuration
public class MockPipelineSpringConfig {
    @Bean("pipelineService")
    @Profile(StroomSpringProfiles.TEST)
    public MockPipelineService mockPipelineService() {
        return new MockPipelineService();
    }

    @Bean
    @Profile(StroomSpringProfiles.TEST)
    public MockTextConverterService mockTextConverterService(final ImportExportHelper importExportHelper) {
        return new MockTextConverterService(importExportHelper);
    }

    @Bean
    @Profile(StroomSpringProfiles.TEST)
    public MockXSLTService mockXSLTService(final ImportExportHelper importExportHelper) {
        return new MockXSLTService(importExportHelper);
    }
}