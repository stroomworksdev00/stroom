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

import java.text.ParseException;
import java.util.function.Supplier;

abstract class AbstractIsFunction extends AbstractFunction {

    private Generator gen;
    private Function function;
    private boolean hasAggregate;

    AbstractIsFunction(final String name) {
        super(name, 1, 1);
    }

    @Override
    public void setParams(final Param[] params) throws ParseException {
        super.setParams(params);

        final Param param = params[0];
        if (param instanceof Function) {
            function = (Function) param;
            hasAggregate = function.hasAggregate();
        } else if (param instanceof Val) {
            // Static computation.
            gen = new StaticValueGen(getTest().test((Val) param));
        } else {
            throw new RuntimeException("Unexpected type [" + param.getClass().getSimpleName() + "]");
        }
    }

    @Override
    public Generator createGenerator() {
        if (gen != null) {
            return gen;
        }

        final Generator childGenerator = function.createGenerator();
        return new Gen(childGenerator, getTest());
    }

    abstract Test getTest();

    @Override
    public boolean hasAggregate() {
        return hasAggregate;
    }

    @Override
    public boolean requiresChildData() {
        if (function != null) {
            return function.requiresChildData();
        }
        return super.requiresChildData();
    }

    @Override
    public Type getCommonReturnType() {
        return Type.BOOLEAN;
    }

    interface Test {

        Val test(Val val);
    }

    private static final class Gen extends AbstractSingleChildGenerator {

        private final Test test;

        Gen(final Generator childGenerator, final Test test) {
            super(childGenerator);
            this.test = test;
        }

        @Override
        public void set(final Val[] values, final StoredValues storedValues) {
            childGenerator.set(values, storedValues);
        }

        @Override
        public Val eval(final StoredValues storedValues, final Supplier<ChildData> childDataSupplier) {
            return test.test(childGenerator.eval(storedValues, childDataSupplier));
        }
    }
}
