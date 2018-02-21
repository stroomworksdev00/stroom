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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import stroom.io.IgnoreCloseWriter;
import stroom.pipeline.server.LocationFactory;
import stroom.pipeline.server.errorhandler.ErrorReceiverProxy;
import stroom.pipeline.server.errorhandler.LoggedException;
import stroom.pipeline.server.factory.ConfigurableElement;
import stroom.pipeline.server.factory.PipelineProperty;
import stroom.pipeline.shared.ElementIcons;
import stroom.pipeline.shared.data.PipelineElementType;
import stroom.pipeline.shared.data.PipelineElementType.Category;
import stroom.xml.converter.json.JSONParser;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Writes out XML and records segment boundaries as it goes.
 */
@ConfigurableElement(
        type = "JSONWriter",
        category = Category.WRITER,
        roles = {PipelineElementType.ROLE_TARGET,
                PipelineElementType.ROLE_WRITER,
                PipelineElementType.ROLE_MUTATOR,
                PipelineElementType.VISABILITY_STEPPING},
        icon = ElementIcons.JSON)
public class JSONWriter extends AbstractWriter {
    private final boolean addTrailingRootValueSeparator = true;
    private final String rootValueSeparator = "\n";
    private final Deque<String> elements = new ArrayDeque<>();
    private final StringBuilder buffer = new StringBuilder();
    private final JsonFactory jsonFactory = new JsonFactory();
    private JsonGenerator jsonGenerator;
    private boolean doneElement;
    private int depth;
    private boolean indentOutput = false;
    private String currentKey;

    @Inject
    public JSONWriter(final ErrorReceiverProxy errorReceiverProxy) {
        super(errorReceiverProxy);
    }

    private JsonGenerator createGenerator() {
        JsonGenerator jsonGenerator = null;

        try {
            jsonGenerator = jsonFactory.createGenerator(new IgnoreCloseWriter(getWriter()));
            if (indentOutput) {
                jsonGenerator.useDefaultPrettyPrinter();
            }

            // Configure the jsonGenerator to add a root value separator unless
            // we want trailing ones in which case we will add them ourselves on
            // endElement().
            if (addTrailingRootValueSeparator) {
                jsonGenerator.setRootValueSeparator(null);
            } else {
                jsonGenerator.setRootValueSeparator(new SerializedString(rootValueSeparator));
            }
        } catch (final Throwable e) {
            fatal(e);
            throw new LoggedException(e.getMessage(), e);
        }

        return jsonGenerator;
    }

    /**
     * Called by the pipeline when processing of a file is complete.
     *
     * @see stroom.pipeline.server.filter.AbstractXMLFilter#endProcessing()
     */
    @Override
    public void endProcessing() {
        try {
            if (jsonGenerator != null) {
                jsonGenerator.close();
                jsonGenerator = null;
            }
        } catch (final Throwable e) {
            fatal(e);
        } finally {
            super.endProcessing();
        }
    }

    /**
     * @param uri       the Namespace URI, or the empty string if the element has no
     *                  Namespace URI or if Namespace processing is not being
     *                  performed
     * @param localName the local name (without prefix), or the empty string if
     *                  Namespace processing is not being performed
     * @param qName     the qualified name (with prefix), or the empty string if
     *                  qualified names are not available
     * @param atts      the attributes attached to the element. If there are no
     *                  attributes, it shall be an empty Attributes object. The value
     *                  of this object after startElement returns is undefined
     * @throws org.xml.sax.SAXException any SAX exception, possibly wrapping another exception
     * @see #endElement
     * @see org.xml.sax.Attributes
     * @see org.xml.sax.helpers.AttributesImpl
     * @see stroom.pipeline.server.filter.AbstractXMLFilter#startElement(java.lang.String,
     * java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes atts)
            throws SAXException {
        try {
            // if we are starting an element then we are actually writing XML so
            // switch the handler.
            if (!doneElement) {
                doneElement = true;
                jsonGenerator = createGenerator();
            }

            // If depth is 0 then we are entering an event.
            if (depth == 0) {
                borrowDestinations(null, null);
            }

            // Increase the element depth.
            depth++;

            if (jsonGenerator != null) {
                currentKey = atts.getValue(JSONParser.XML_ATTRIBUTE_KEY);

                if (localName.equals(JSONParser.XML_ELEMENT_MAP)) {
                    elements.push(localName);
                    writeKey();
                    jsonGenerator.writeStartObject();
                } else if (localName.equals(JSONParser.XML_ELEMENT_ARRAY)) {
                    elements.push(localName);
                    writeKey();
                    jsonGenerator.writeStartArray();
                }
            }

        } catch (final Throwable e) {
            error(e);
        } finally {
            buffer.setLength(0);
            super.startElement(uri, localName, qName, atts);
        }
    }

    /**
     * @param uri       the Namespace URI, or the empty string if the element has no
     *                  Namespace URI or if Namespace processing is not being
     *                  performed
     * @param localName the local name (without prefix), or the empty string if
     *                  Namespace processing is not being performed
     * @param qName     the qualified XML name (with prefix), or the empty string if
     *                  qualified names are not available
     * @throws org.xml.sax.SAXException any SAX exception, possibly wrapping another exception
     * @see stroom.pipeline.server.filter.AbstractXMLFilter#endElement(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        try {
            final boolean inMap = JSONParser.XML_ELEMENT_MAP.equals(elements.peek());

            if (jsonGenerator != null) {
                if (localName.equals(JSONParser.XML_ELEMENT_MAP)) {
                    jsonGenerator.writeEndObject();
                    elements.pop();
                } else if (localName.equals(JSONParser.XML_ELEMENT_ARRAY)) {
                    jsonGenerator.writeEndArray();
                    elements.pop();
                } else if (localName.equals(JSONParser.XML_ELEMENT_STRING)) {
                    final String value = buffer.toString();
                    if (value.length() > 0) {
                        if (inMap) {
                            if (writeKey()) {
                                jsonGenerator.writeString(value);
                            }
                        } else {
                            jsonGenerator.writeString(value);
                        }
                    }
                } else if (localName.equals(JSONParser.XML_ELEMENT_NUMBER)) {
                    final String value = buffer.toString();
                    if (value.length() > 0) {
                        if (inMap) {
                            if (writeKey()) {
                                jsonGenerator.writeNumber(value);
                            }
                        } else {
                            jsonGenerator.writeNumber(value);
                        }
                    }
                } else if (localName.equals(JSONParser.XML_ELEMENT_BOOLEAN)) {
                    final String value = buffer.toString();
                    if (value.length() > 0) {
                        if (inMap) {
                            if (writeKey()) {
                                jsonGenerator.writeBoolean(Boolean.parseBoolean(value));
                            }
                        } else {
                            jsonGenerator.writeBoolean(Boolean.parseBoolean(value));
                        }
                    }
                } else if (localName.equals(JSONParser.XML_ELEMENT_NULL)) {
                    if (inMap) {
                        if (writeKey()) {
                            jsonGenerator.writeNull();
                        }
                    } else {
                        jsonGenerator.writeNull();
                    }
                }
            }

            // Decrease the element depth
            depth--;

            // If depth = 0 then we have finished an event.
            if (depth == 0) {
                try {
                    // Insert root value separator if we are adding a trailing
                    // one, otherwise the jsonGenerator will have been
                    // configured to add one itself.
                    if (addTrailingRootValueSeparator) {
                        if (jsonGenerator != null) {
                            jsonGenerator.writeRaw(rootValueSeparator);
                        }
                    }

                    jsonGenerator.flush();
                    returnDestinations();
                } catch (final IOException e) {
                    throw new SAXException(e);
                }
            }

        } catch (final Throwable e) {
            error(e);
        } finally {
            clearKey();
            buffer.setLength(0);
            super.endElement(uri, localName, qName);
        }
    }

    /**
     * @param ch     the characters from the XML document
     * @param start  the start position in the array
     * @param length the number of characters to read from the array
     * @throws org.xml.sax.SAXException any SAX exception, possibly wrapping another exception
     * @see #ignorableWhitespace
     * @see org.xml.sax.Locator
     * @see stroom.pipeline.server.filter.AbstractXMLFilter#characters(char[],
     * int, int)
     */
    @Override
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        buffer.append(ch, start, length);
        super.characters(ch, start, length);
    }

    private boolean writeKey() {
        if (currentKey == null) {
            return false;
        }

        try {
            jsonGenerator.writeFieldName(currentKey);
        } catch (final Throwable t) {
            fatal(t);
        }

        currentKey = null;
        return true;
    }

    private void clearKey() {
        currentKey = null;
    }

    public void setLocationFactory(final LocationFactory locationFactory) {
    }

    public boolean isIndentOutput() {
        return indentOutput;
    }

    @PipelineProperty(description = "Should output JSON be indented and include new lines (pretty printed)?", defaultValue = "false")
    public void setIndentOutput(final boolean indentOutput) {
        this.indentOutput = indentOutput;
    }

    @Override
    @PipelineProperty(description = "The output character encoding to use.", defaultValue = "UTF-8")
    public void setEncoding(final String encoding) {
        super.setEncoding(encoding);
    }
}
