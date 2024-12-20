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

package stroom.document.client;

import stroom.explorer.shared.DocumentTypeGroup;
import stroom.svg.shared.SvgImage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class ClientDocumentType {

    @JsonProperty
    private final DocumentTypeGroup group;
    @JsonProperty
    private final String type;
    @JsonProperty
    private final String displayType;
    @JsonProperty
    private final SvgImage icon;

    @JsonCreator
    public ClientDocumentType(@JsonProperty("group") final DocumentTypeGroup group,
                              @JsonProperty("type") final String type,
                              @JsonProperty("displayType") final String displayType,
                              @JsonProperty("icon") final SvgImage icon) {
        this.group = group;
        this.type = type;
        this.displayType = displayType;
        this.icon = icon;
    }

    public DocumentTypeGroup getGroup() {
        return group;
    }

    public String getDisplayType() {
        return displayType;
    }

    public String getType() {
        return type;
    }

    public SvgImage getIcon() {
        return icon;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final ClientDocumentType that = (ClientDocumentType) o;

        return type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    @Override
    public String toString() {
        return type;
    }
}
