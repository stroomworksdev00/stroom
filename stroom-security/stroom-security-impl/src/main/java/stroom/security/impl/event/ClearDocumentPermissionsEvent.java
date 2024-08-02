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

package stroom.security.impl.event;

import stroom.docref.DocRef;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClearDocumentPermissionsEvent implements PermissionChangeEvent {

    @JsonProperty
    private final DocRef docRef;

    @JsonCreator
    public ClearDocumentPermissionsEvent(@JsonProperty("docRef") final DocRef docRef) {
        this.docRef = docRef;
    }

    public static void fire(final PermissionChangeEventBus eventBus, final DocRef docRef) {
        eventBus.fire(new ClearDocumentPermissionsEvent(docRef));
    }

    public DocRef getDocRef() {
        return docRef;
    }
}
