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
 */

package stroom.test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import stroom.security.server.SecuritySpringConfig;
import stroom.util.io.FileUtil;
import stroom.util.spring.StroomSpringProfiles;
import stroom.util.task.TaskScopeContextHolder;

/**
 * Script to create some base data for testing.
 */
public final class SetupSampleData {
    public static void main(final String[] args) throws Exception {
        FileUtil.useDevTempDir();
        System.setProperty("stroom.connectionTesterClassName",
                "stroom.entity.server.util.StroomConnectionTesterOkOnException");

        TaskScopeContextHolder.addContext();
        try {
            @SuppressWarnings("resource") final AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
            appContext.getEnvironment().setActiveProfiles(StroomSpringProfiles.PROD,
                    SecuritySpringConfig.MOCK_SECURITY);
            appContext.register(SetupSampleDataSpringConfig.class);
            appContext.refresh();
            final CommonTestControl commonTestControl = appContext.getBean(CommonTestControl.class);

            commonTestControl.setup();

            final SetupSampleDataBean setupSampleDataBean = appContext.getBean(SetupSampleDataBean.class);
            setupSampleDataBean.run(true);

        } finally {
            TaskScopeContextHolder.removeContext();
        }
    }
}
