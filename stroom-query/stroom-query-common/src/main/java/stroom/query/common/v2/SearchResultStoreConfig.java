package stroom.query.common.v2;

import stroom.util.io.ByteSize;
import stroom.util.shared.IsStroomConfig;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(alphabetic = true)
public class SearchResultStoreConfig extends AbstractResultStoreConfig implements IsStroomConfig {

    public SearchResultStoreConfig() {
        super(10_000,
                true,
                ByteSize.ofMebibytes(1),
                ByteSize.ofGibibytes(1),
                1000,
                10_000,
                500_000,
                ResultStoreLmdbConfig.builder().localDir("search_results").build());
    }

    @JsonCreator
    public SearchResultStoreConfig(@JsonProperty("maxPutsBeforeCommit") final int maxPutsBeforeCommit,
                                   @JsonProperty("offHeapResults") final boolean offHeapResults,
                                   @JsonProperty("minPayloadSize") final ByteSize minPayloadSize,
                                   @JsonProperty("maxPayloadSize") final ByteSize maxPayloadSize,
                                   @JsonProperty("maxStringFieldLength") final int maxStringFieldLength,
                                   @JsonProperty("valueQueueSize") final int valueQueueSize,
                                   @JsonProperty("maxSortedItems") final int maxSortedItems,
                                   @JsonProperty("lmdb") final ResultStoreLmdbConfig lmdbConfig) {
        super(maxPutsBeforeCommit,
                offHeapResults,
                minPayloadSize,
                maxPayloadSize,
                maxStringFieldLength,
                valueQueueSize,
                maxSortedItems,
                lmdbConfig);
    }
}
