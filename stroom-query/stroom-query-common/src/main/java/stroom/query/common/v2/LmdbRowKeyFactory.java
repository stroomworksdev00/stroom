package stroom.query.common.v2;

import stroom.query.api.v2.TimeFilter;

import org.lmdbjava.KeyRange;

import java.nio.ByteBuffer;

public interface LmdbRowKeyFactory {

    /**
     * Create an LMDB row key.
     *
     * @param depth           The nesting depth of the key.
     * @param parentGroupHash The hash of the parent group.
     * @param groupHash       The hash of the current group.
     * @param timeMs          The time if needed.
     * @return A new LMDB row key.
     */
    ByteBuffer create(int depth,
                      long parentGroupHash,
                      long groupHash,
                      long timeMs);

    /**
     * Change a specific part of the supplied keys byte buffer to ensure it is unique if necessary.
     *
     * @param rowKey The row key to change.
     * @return A row key that has been altered to make it unique if necessary.
     */
    LmdbKV makeUnique(LmdbKV rowKey);

    /**
     * Determine if the supplied row key is a grouping key.
     *
     * @param rowKey The row key to test.
     * @return True if the supplied key is a grouping key.
     */
    boolean isGroup(LmdbKV rowKey);

    /**
     * Create a key range to filter rows to find the children of the supplied parent key.
     *
     * @param parentKey The parent key to create the child key range for.
     * @return A key range to filter rows to find the children of the supplied parent key.
     */
    KeyRange<ByteBuffer> createChildKeyRange(Key parentKey);

    /**
     * Create a key range to filter rows to find the children of the supplied parent key that also filters by time.
     *
     * @param parentKey  The parent key to create the child key range for.
     * @param timeFilter The time filter to apply to the key range.
     * @return A key range to filter rows to find the children of the supplied parent key that also filters by time.
     */
    KeyRange<ByteBuffer> createChildKeyRange(Key parentKey, TimeFilter timeFilter);
}
