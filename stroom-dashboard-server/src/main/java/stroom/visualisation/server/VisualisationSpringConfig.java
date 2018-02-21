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

package stroom.visualisation.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import stroom.entity.server.util.StroomEntityManager;
import stroom.explorer.server.ExplorerActionHandlers;
import stroom.importexport.server.ImportExportActionHandlers;
import stroom.importexport.server.ImportExportHelper;
import stroom.security.SecurityContext;
import stroom.util.spring.StroomSpringProfiles;
import stroom.visualisation.server.VisualisationService;
import stroom.visualisation.server.VisualisationServiceImpl;
import stroom.visualisation.shared.Visualisation;

import javax.inject.Inject;

@Configuration
public class VisualisationSpringConfig {
    @Inject
    public VisualisationSpringConfig(final ExplorerActionHandlers explorerActionHandlers,
                                     final ImportExportActionHandlers importExportActionHandlers,
                                     final VisualisationService visualisationService) {
        explorerActionHandlers.add(9, Visualisation.ENTITY_TYPE, Visualisation.ENTITY_TYPE, visualisationService);
        importExportActionHandlers.add(Visualisation.ENTITY_TYPE, visualisationService);
    }

    @Bean("visualisationService")
    @Profile(StroomSpringProfiles.PROD)
    public VisualisationService visualisationService(final StroomEntityManager entityManager,
                                                     final ImportExportHelper importExportHelper,
                                                     final SecurityContext securityContext) {
        return new VisualisationServiceImpl(entityManager, importExportHelper, securityContext);
    }
}