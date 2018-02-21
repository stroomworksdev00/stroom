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

package stroom.pipeline.state;

import stroom.streamtask.shared.StreamProcessor;
import stroom.streamtask.shared.StreamTask;

public class StreamProcessorHolder implements Holder {
    private StreamProcessor streamProcessor;
    private StreamTask streamTask;

    public StreamProcessor getStreamProcessor() {
        return streamProcessor;
    }

    public StreamTask getStreamTask() {
        return streamTask;
    }

    public void setStreamProcessor(final StreamProcessor streamProcessor, final StreamTask streamTask) {
        this.streamProcessor = streamProcessor;
        this.streamTask = streamTask;
    }
}
