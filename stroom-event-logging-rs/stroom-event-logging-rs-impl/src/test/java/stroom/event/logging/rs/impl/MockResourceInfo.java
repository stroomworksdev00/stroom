/*
 * Copyright 2020 Crown Copyright
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

package stroom.event.logging.rs.impl;

import java.lang.reflect.Method;
import jakarta.ws.rs.container.ResourceInfo;

public class MockResourceInfo implements ResourceInfo {

    private Class<?> resourceClass;
    private Method method;

    public void setResourceClass(final Class<?> resourceClass) {
        this.resourceClass = resourceClass;
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public Method getResourceMethod() {
        return method;
    }

    @Override
    public Class<?> getResourceClass() {
        return resourceClass;
    }
}
