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

package stroom.test;

import stroom.security.api.SecurityContext;
import stroom.test.common.util.test.StroomTest;
import stroom.util.io.FileUtil;
import stroom.util.io.TempDirProvider;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import java.nio.file.Path;
import java.util.Objects;
import javax.inject.Inject;

/**
 * This class should be common to all component and integration tests.
 */
public abstract class StroomIntegrationTest implements StroomTest {

    private Path testTempDir;

    @Inject
    private CommonTestControl commonTestControl;
    @Inject
    private SecurityContext securityContext;
    @Inject
    private TempDirProvider tempDirProvider;

    static Path tempDir; // Static makes the temp dir remain constant for the life of the test class.

    private static final ThreadLocal<Class<?>> CURRENT_TEST_CLASS_THREAD_LOCAL = new ThreadLocal<>();
    private static CommonTestControl currentCommonTestControl;
    private static SecurityContext currentSecurityContext;

    private boolean performSetupOrCleanup(final TestInfo testInfo) {
        final Class<?> testClass = testInfo.getTestClass().orElse(null);
        final Class<?> currentTestClass = CURRENT_TEST_CLASS_THREAD_LOCAL.get();
        CURRENT_TEST_CLASS_THREAD_LOCAL.set(testClass);
        return setupBetweenTests() || testClass == null || !Objects.equals(testClass, currentTestClass);
    }

    /**
     * Initialise required database entities.
     */
    @BeforeEach
    final void setup(final TestInfo testInfo) {
        currentCommonTestControl = commonTestControl;
        currentSecurityContext = securityContext;

        if (performSetupOrCleanup(testInfo)) {
            tempDir = tempDirProvider.get();
            if (tempDir == null) {
                throw new NullPointerException("Temp dir is null");
            }
            this.testTempDir = tempDir;
            securityContext.asProcessingUser(() -> commonTestControl.setup(tempDir));
        }
    }

    @AfterEach
    final void cleanup(final TestInfo testInfo) {
        if (performSetupOrCleanup(testInfo)) {
            securityContext.asProcessingUser(() -> commonTestControl.cleanup());
            // We need to delete the contents of the temp dir here as it is the same for the whole of a test class.
            FileUtil.deleteContents(tempDir);
        }
    }

    @AfterAll
    static void cleanupAll() {
        final SecurityContext securityContext = currentSecurityContext;
        final CommonTestControl commonTestControl = currentCommonTestControl;
        if (securityContext != null && commonTestControl != null) {
            securityContext.asProcessingUser(commonTestControl::cleanup);
            // We need to delete the contents of the temp dir here as it is the same for the whole of a test class.
            FileUtil.deleteContents(tempDir);
        }
    }

    @Override
    public Path getCurrentTestDir() {
        return testTempDir;
    }

    protected boolean setupBetweenTests() {
        return true;
    }
}
