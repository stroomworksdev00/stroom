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

package stroom.core.servlet;

import stroom.ui.config.shared.UiConfig;
import stroom.ui.config.shared.UserPreferencesService;
import stroom.util.shared.IsServlet;

import jakarta.inject.Inject;

import java.util.Set;

public class DashboardServlet extends AppServlet implements IsServlet {

    private static final Set<String> PATH_SPECS = Set.of("/dashboard");

    @Inject
    DashboardServlet(final UiConfig uiConfig,
                     final UserPreferencesService userPreferencesService) {
        super(uiConfig, userPreferencesService);
    }

    String getScript() {
        return "ui/dashboard/dashboard.nocache.js";
    }

    @Override
    public Set<String> getPathSpecs() {
        return PATH_SPECS;
    }
}
