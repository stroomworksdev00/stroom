package stroom.search.extraction;

import stroom.dashboard.expression.v1.FieldIndex;
import stroom.dashboard.expression.v1.Val;
import stroom.dashboard.expression.v1.ValuesConsumer;
import stroom.query.api.v2.QueryKey;
import stroom.util.pipeline.scope.PipelineScoped;

import javax.inject.Inject;

@PipelineScoped
public class ValueConsumerHolder implements ValuesConsumer {

    private final ExtractionState extractionState;
    private QueryKey queryKey;
    private ValuesConsumer receiver;
    private FieldIndex fieldIndex;

    @Inject
    ValueConsumerHolder(final ExtractionState extractionState) {
        this.extractionState = extractionState;
    }

    @Override
    public void accept(final Val[] values) {
        receiver.accept(values);
        extractionState.incrementCount();
    }

    public QueryKey getQueryKey() {
        return queryKey;
    }

    public void setQueryKey(final QueryKey queryKey) {
        this.queryKey = queryKey;
    }

    public void setReceiver(final ValuesConsumer receiver) {
        this.receiver = receiver;
    }

    public FieldIndex getFieldIndex() {
        return fieldIndex;
    }

    public void setFieldIndex(final FieldIndex fieldIndex) {
        this.fieldIndex = fieldIndex;
    }
}
