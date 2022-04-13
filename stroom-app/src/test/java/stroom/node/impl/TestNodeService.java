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

package stroom.node.impl;


import stroom.node.api.FindNodeCriteria;
import stroom.node.api.NodeService;
import stroom.test.AbstractCoreIntegrationTest;
import stroom.util.logging.LambdaLogger;
import stroom.util.logging.LambdaLoggerFactory;

import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

class TestNodeService extends AbstractCoreIntegrationTest {

    private static final LambdaLogger LOGGER = LambdaLoggerFactory.getLogger(TestNodeService.class);

    @Inject
    private NodeService nodeService;

    @Test
    void testBasic() {
        LOGGER.info("Before test");
        LOGGER.info("nodeService = " + nodeService);
        assertThat(nodeService.findNodeNames(new FindNodeCriteria()).size() > 0).isTrue();
        LOGGER.info("After test");
    }

    @Test
    void testAgain() {
        LOGGER.info("Before test 2");
        LOGGER.info("nodeService = " + nodeService);
        assertThat(nodeService.findNodeNames(new FindNodeCriteria()).size() > 0).isTrue();
        LOGGER.info("After test 2");
    }
}
