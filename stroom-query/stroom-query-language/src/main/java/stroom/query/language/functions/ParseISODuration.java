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

package stroom.query.language.functions;

import stroom.query.language.functions.ref.StoredValues;
import stroom.query.language.token.Param;

import java.text.ParseException;
import java.util.function.Supplier;

@SuppressWarnings("unused") //Used by FunctionFactory
@FunctionDef(
        name = ParseISODuration.NAME,
        commonCategory = FunctionCategory.DATE,
        commonSubCategories = "Duration",
        commonReturnType = ValDuration.class,
        commonReturnDescription = "The parsed ISO duration.",
        signatures = {
                @FunctionSignature(
                        description = "Parses the supplied value as an ISO duration, e.g. PTH3",
                        args = @FunctionArg(
                                name = "value",
                                argType = ValString.class,
                                description = "The duration as a string."))
        })
class ParseISODuration extends AbstractFunction {

    static final String NAME = "parseISODuration";

    private Generator gen;
    private Function function;
    private boolean hasAggregate;

    public ParseISODuration(final String name) {
        super(name, 1, 1);
    }

    @Override
    public void setParams(final Param[] params) throws ParseException {
        super.setParams(params);

        final Param param = params[0];
        if (param instanceof Function) {
            function = (Function) param;
            hasAggregate = function.hasAggregate();
        } else {
            final String durationString = param.toString();
            gen = new StaticValueFunction(ValDurationUtil.parseISODuration(durationString)).createGenerator();
        }
    }

    @Override
    public Generator createGenerator() {
        if (gen != null) {
            return gen;
        }

        final Generator childGenerator = function.createGenerator();
        return new Gen(childGenerator);
    }

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
        return Type.DURATION;
    }

    private static final class Gen extends AbstractSingleChildGenerator {

        Gen(final Generator childGenerator) {
            super(childGenerator);
        }

        @Override
        public void set(final Val[] values, final StoredValues storedValues) {
            childGenerator.set(values, storedValues);
        }

        @Override
        public Val eval(final StoredValues storedValues, final Supplier<ChildData> childDataSupplier) {
            final Val val = childGenerator.eval(storedValues, childDataSupplier);
            return ValDurationUtil.parseISODuration(val);
        }
    }
}
