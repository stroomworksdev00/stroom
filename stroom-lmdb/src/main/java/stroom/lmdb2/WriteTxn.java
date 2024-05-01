package stroom.lmdb2;

import org.lmdbjava.Env;
import org.lmdbjava.Txn;

import java.nio.ByteBuffer;

public class WriteTxn extends AbstractTxn {

    private Txn<ByteBuffer> txn;

    WriteTxn(final Env<ByteBuffer> env, final LmdbErrorHandler lmdbErrorHandler) {
        super(env, lmdbErrorHandler);
    }

    @Override
    synchronized Txn<ByteBuffer> get() {
        checkThread();
        try {
            if (txn == null) {
                txn = env.txnWrite();
            }
            return txn;
        } catch (final RuntimeException e) {
            lmdbErrorHandler.error(e);
            throw e;
        }
    }

    public synchronized void commit() {
        checkThread();
        try {
            if (txn != null) {
                txn.commit();
            }
        } catch (final RuntimeException e) {
            lmdbErrorHandler.error(e);
            throw e;
        } finally {
            try {
                if (txn != null) {
                    txn.close();
                }
            } catch (final RuntimeException e) {
                lmdbErrorHandler.error(e);
            } finally {
                txn = null;
            }
        }
    }

    @Override
    public synchronized void close() {
        checkThread();
        try {
            if (txn != null) {
                txn.close();
            }
        } catch (final RuntimeException e) {
            lmdbErrorHandler.error(e);
            throw e;
        } finally {
            txn = null;
        }
    }
}
