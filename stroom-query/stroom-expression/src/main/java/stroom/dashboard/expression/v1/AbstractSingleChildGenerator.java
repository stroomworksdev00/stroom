/*
 * Copyright 2017 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package stroom.dashboard.expression.v1;

import stroom.dashboard.expression.v1.ref.StoredValues;

import java.util.function.Supplier;

abstract class AbstractSingleChildGenerator extends AbstractGenerator {

    final Generator childGenerator;

    AbstractSingleChildGenerator(final Generator childGenerator) {
        this.childGenerator = childGenerator;
    }

    @Override
    public abstract void set(Val[] values, final StoredValues storedValues);

    @Override
    public abstract Val eval(final StoredValues storedValues, final Supplier<ChildData> childDataSupplier);

    @Override
    public void merge(final StoredValues existingValues, final StoredValues newValues) {
        addChildren(existingValues, newValues);
    }

    private void addChildren(final StoredValues existingValues, final StoredValues newValues) {
        childGenerator.merge(existingValues, newValues);
    }
}
