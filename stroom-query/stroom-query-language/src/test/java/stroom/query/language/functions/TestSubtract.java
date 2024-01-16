package stroom.query.language.functions;

import stroom.util.date.DateUtil;

import java.time.Duration;
import java.util.stream.Stream;

class TestSubtract extends AbstractFunctionTest<Subtract> {

    @Override
    Class<Subtract> getFunctionType() {
        return Subtract.class;
    }

    @Override
    Stream<TestCase> getTestCases() {
        return Stream.of(
                TestCase.of(
                        "Numeric 1",
                        ValDouble.create(2),
                        ValLong.create(5),
                        ValLong.create(3)),
                TestCase.of(
                        "Numeric 2",
                        ValDouble.create(0),
                        ValLong.create(5),
                        ValLong.create(5)),
                TestCase.of(
                        "Numeric 3",
                        ValDouble.create(-3),
                        ValLong.create(2),
                        ValLong.create(5)),
                // Subtract is a numeric function so subtraction of dates gives an error
                TestCase.of(
                        "2",
                        ValErr.INSTANCE,
                        ValString.create("2008-11-18T09:47:50.500"),
                        ValString.create("2008-11-18T09:47:50.100")),
                TestCase.of(
                        "2",
                        ValErr.INSTANCE,
                        ValString.create("2008-11-18T09:47:50.548Z"),
                        ValString.create("2008-11-18T09:47:50.548Z")),
                TestCase.of(
                        "duration",
                        ValDuration.create(Duration.ofMinutes(5).toMillis()),
                        ValDuration.create(Duration.ofMinutes(7).toMillis()),
                        ValDuration.create(Duration.ofMinutes(2).toMillis())),
                TestCase.of(
                        "date",
                        ValDate.create(stroom.util.date.DateUtil.parseNormalDateTimeString("2020-10-01T00:02:00.000Z")),
                        ValDate.create(DateUtil.parseNormalDateTimeString("2020-10-01T00:04:00.000Z")),
                        ValDuration.create(Duration.ofMinutes(2).toMillis()))
        );
    }
}
