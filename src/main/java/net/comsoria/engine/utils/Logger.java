package net.comsoria.engine.utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static MultiOutputStream outputStream = new MultiOutputStream(System.out);
    private static PrintStream stream = new PrintStream(outputStream);

    static {

    }

    public static void log(String msg) throws IOException {
        log(msg, LogType.INFO);
    }

    public static void log(String msg, LogType type) throws IOException {
        msg = "[" + type.toString() + ": " + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "]: " + msg;

//        System.out.println(msg);
        stream.println(msg);
    }

    public static void addOutputStream(OutputStream stream) {
        Logger.outputStream.outputs.add(stream);
//        System.setOut(stream);
//        System.setErr(stream);
    }

    public static void logRaw(String msg) {
        stream.println(msg);
    }

    public enum LogType {
        INFO,
        WARN,
        ERROR
    }
}
