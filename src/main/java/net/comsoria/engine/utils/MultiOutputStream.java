package net.comsoria.engine.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiOutputStream extends OutputStream {
    public List<OutputStream> outputs = new ArrayList<>();

    public MultiOutputStream(OutputStream... streams) {
        outputs.addAll(Arrays.asList(streams));
    }

    @Override public void write(int b) throws IOException {
        for (OutputStream outputStream : outputs) outputStream.write(b);
    }

    @Override public void write(byte[] b) throws IOException {
        for (OutputStream outputStream : outputs) outputStream.write(b);
    }

    @Override public void write(byte[] b, int off, int len) throws IOException {
        for (OutputStream outputStream : outputs) outputStream.write(b, off, len);
    }


    @Override
    public void flush() throws IOException {
        for (OutputStream outputStream : outputs) outputStream.flush();
    }

    @Override
    public void close() throws IOException {
        for (OutputStream outputStream : outputs) {
            try {
                outputStream.close();
            } catch (Exception e) {
                Logger.log(e.getLocalizedMessage(), Logger.LogType.ERROR);
            }
        }
    }


}
