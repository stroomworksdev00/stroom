package stroom.dashboard.expression.v1;

import stroom.dashboard.expression.v1.ref.CountIterationReference;
import stroom.dashboard.expression.v1.ref.CountReference;
import stroom.dashboard.expression.v1.ref.DoubleListReference;
import stroom.dashboard.expression.v1.ref.FieldValReference;
import stroom.dashboard.expression.v1.ref.MyByteBufferOutput;
import stroom.dashboard.expression.v1.ref.RandomValReference;
import stroom.dashboard.expression.v1.ref.StoredValues;
import stroom.dashboard.expression.v1.ref.StringListReference;
import stroom.dashboard.expression.v1.ref.ValListReference;
import stroom.dashboard.expression.v1.ref.ValReference;
import stroom.dashboard.expression.v1.ref.ValueReferenceIndex;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.List;

public class TestValueReferenceIndex {

    @Test
    void testCountReference() {
        final ValueReferenceIndex valueReferenceIndex = new ValueReferenceIndex();
        final CountReference r1 = valueReferenceIndex.addCount("test1");
        final CountReference r2 = valueReferenceIndex.addCount("test2");
        final CountReference r3 = valueReferenceIndex.addCount("test3");
        final StoredValues storedValues = valueReferenceIndex.createStoredValues();
        r1.increment(storedValues);
        r2.add(storedValues, 3);
        r3.increment(storedValues);
        testWriteRead(valueReferenceIndex, storedValues);
    }

    @Test
    void testCountIterationReference() {
        final ValueReferenceIndex valueReferenceIndex = new ValueReferenceIndex();
        final CountIterationReference r1 = valueReferenceIndex.addCountIteration("test1", 1);
        final CountIterationReference r2 = valueReferenceIndex.addCountIteration("test2", 2);
        final CountIterationReference r3 = valueReferenceIndex.addCountIteration("test3", 3);
        final StoredValues storedValues = valueReferenceIndex.createStoredValues();
        r1.increment(storedValues);
        r2.add(storedValues, 3);
        r3.increment(storedValues);
        testWriteRead(valueReferenceIndex, storedValues);
    }

    @Test
    void testDoubleListReference() {
        final ValueReferenceIndex valueReferenceIndex = new ValueReferenceIndex();
        final DoubleListReference r1 = valueReferenceIndex.addDoubleList("test1");
        final DoubleListReference r2 = valueReferenceIndex.addDoubleList("test2");
        final DoubleListReference r3 = valueReferenceIndex.addDoubleList("test3");
        final StoredValues storedValues = valueReferenceIndex.createStoredValues();
        r1.set(storedValues, List.of(10D, 20D, 30D));
        r2.set(storedValues, List.of(40D, 50D, 60D));
        r3.set(storedValues, List.of(0.1D, 0.2D, 0.3D));
        testWriteRead(valueReferenceIndex, storedValues);
    }

    @Test
    void testStringListReference() {
        final ValueReferenceIndex valueReferenceIndex = new ValueReferenceIndex();
        final StringListReference r1 = valueReferenceIndex.addStringList("test1");
        final StringListReference r2 = valueReferenceIndex.addStringList("test2");
        final StringListReference r3 = valueReferenceIndex.addStringList("test3");
        final StoredValues storedValues = valueReferenceIndex.createStoredValues();
        r1.set(storedValues, List.of("one", "two", "three"));
        r2.set(storedValues, List.of("four", "five", "six"));
        r3.set(storedValues, List.of("seven", "eight", "nine"));
        testWriteRead(valueReferenceIndex, storedValues);
    }

    @Test
    void testValListReference() {
        final ValueReferenceIndex valueReferenceIndex = new ValueReferenceIndex();
        final ValListReference r1 = valueReferenceIndex.addValList("test1");
        final ValListReference r2 = valueReferenceIndex.addValList("test2");
        final ValListReference r3 = valueReferenceIndex.addValList("test3");
        final StoredValues storedValues = valueReferenceIndex.createStoredValues();
        r1.set(storedValues, List.of(ValString.create("one"), ValDouble.create(0.1D), ValNull.INSTANCE));
        r2.set(storedValues, List.of(ValString.create("test"), ValNull.INSTANCE, ValErr.create("err")));
        r3.set(storedValues, List.of(ValNull.INSTANCE, ValString.create("test"), ValInteger.create(1)));
        testWriteRead(valueReferenceIndex, storedValues);
    }

    @Test
    void testFieldValReference() {
        final ValueReferenceIndex valueReferenceIndex = new ValueReferenceIndex();
        final FieldValReference r1 = valueReferenceIndex.addFieldValue("test1", 0);
        final FieldValReference r2 = valueReferenceIndex.addFieldValue("test2", 1);
        final FieldValReference r3 = valueReferenceIndex.addFieldValue("test3", 2);
        final StoredValues storedValues = valueReferenceIndex.createStoredValues();
        r1.set(storedValues, ValString.create("one"));
        r2.set(storedValues, ValString.create("two"));
        r3.set(storedValues, ValNull.INSTANCE);
        testWriteRead(valueReferenceIndex, storedValues);
    }

    @Test
    void testRandomValReference() {
        final ValueReferenceIndex valueReferenceIndex = new ValueReferenceIndex();
        final RandomValReference r1 = valueReferenceIndex.addRandomValue("test1");
        final RandomValReference r2 = valueReferenceIndex.addRandomValue("test2");
        final RandomValReference r3 = valueReferenceIndex.addRandomValue("test3");
        final StoredValues storedValues = valueReferenceIndex.createStoredValues();
        r1.set(storedValues, ValString.create("one"));
        r2.set(storedValues, ValString.create("two"));
        r3.set(storedValues, ValNull.INSTANCE);
        testWriteRead(valueReferenceIndex, storedValues);
    }

    @Test
    void testValReference() {
        final ValueReferenceIndex valueReferenceIndex = new ValueReferenceIndex();
        final ValReference r1 = valueReferenceIndex.addValue("test1");
        final ValReference r2 = valueReferenceIndex.addValue("test2");
        final ValReference r3 = valueReferenceIndex.addValue("test3");
        final StoredValues storedValues = valueReferenceIndex.createStoredValues();
        r1.set(storedValues, ValString.create("one"));
        r2.set(storedValues, ValString.create("two"));
        r3.set(storedValues, ValNull.INSTANCE);
        testWriteRead(valueReferenceIndex, storedValues);
    }

    private void testWriteRead(final ValueReferenceIndex valueReferenceIndex, final StoredValues storedValues) {
        final ByteBuffer byteBuffer1 = write(valueReferenceIndex, storedValues);
        StoredValues storedValues2 = valueReferenceIndex.read(byteBuffer1);
        final ByteBuffer byteBuffer2 = write(valueReferenceIndex, storedValues2);
        StoredValues storedValues3 = valueReferenceIndex.read(byteBuffer2);
    }

    private ByteBuffer write(final ValueReferenceIndex valueReferenceIndex, final StoredValues storedValues) {
        try (final MyByteBufferOutput output = new MyByteBufferOutput(16, -1)) {
            valueReferenceIndex.write(storedValues, output);
            output.flush();

            return output.getByteBuffer().flip();
        }
    }
}
