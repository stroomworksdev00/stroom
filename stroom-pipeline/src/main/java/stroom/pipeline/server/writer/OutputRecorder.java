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

package stroom.pipeline.server.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stroom.pipeline.destination.Destination;
import stroom.pipeline.server.stepping.Recorder;
import stroom.util.io.StreamUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class OutputRecorder extends AbstractDestinationProvider implements Recorder {
    private static final Logger LOGGER = LoggerFactory.getLogger(OutputRecorder.class);
    private final MemoryDestination destination = new MemoryDestination();
    private String elementId;

    @Override
    public void startProcessing() {
    }

    @Override
    public void endProcessing() {
    }

    @Override
    public void startStream() {
    }

    @Override
    public void endStream() {
    }

    @Override
    public Destination borrowDestination() throws IOException {
        return destination;
    }

    @Override
    public void returnDestination(final Destination destination) throws IOException {
    }

    @Override
    public Object getData() {
        return destination.getData();
    }

    @Override
    public void clear() {
        destination.clear();
    }

    @Override
    public String getElementId() {
        return elementId;
    }

    @Override
    public void setElementId(final String elementId) {
        this.elementId = elementId;
    }

    private static class MemoryDestination implements Destination {
        private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(4096);
        private byte[] header;
        private byte[] footer;

        @Override
        public OutputStream getByteArrayOutputStream() throws IOException {
            return outputStream;
        }

        @Override
        public OutputStream getOutputStream(final byte[] header, final byte[] footer) throws IOException {
            this.header = header;
            this.footer = footer;
            return outputStream;
        }

        public Object getData() {
            try {
                outputStream.flush();

                int size = 0;
                if (header != null) {
                    size += header.length;
                }
                if (footer != null) {
                    size += footer.length;
                }
                size += outputStream.size();

                final ByteArrayOutputStream copy = new ByteArrayOutputStream(size);
                if (header != null) {
                    copy.write(header);
                }
                outputStream.writeTo(copy);
                if (footer != null) {
                    copy.write(footer);
                }
                copy.close();
                return copy.toString(StreamUtil.DEFAULT_CHARSET_NAME);
            } catch (final IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
            return null;
        }

        public void clear() {
            outputStream.reset();
        }
    }
}
