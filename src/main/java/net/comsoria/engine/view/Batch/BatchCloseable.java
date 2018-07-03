package net.comsoria.engine.view.Batch;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public class BatchCloseable implements Closeable {
    private final List<Closeable> toClose;

    public BatchCloseable(List<Closeable> toClose) {
        this.toClose = toClose;
    }

    @Override public void close() throws IOException {
        for (Closeable closeable : toClose) closeable.close();
    }
}
