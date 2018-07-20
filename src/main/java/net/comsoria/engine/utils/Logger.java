package net.comsoria.engine.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    public static void log(String msg) {
        log(msg, LogType.INFO);
    }

    public static void log(String msg, LogType type) {
        System.out.println("[" + type.toString() + ": " + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "]: " + msg);
    }

    public enum LogType {
        INFO,
        WARN,
        ERROR
    }
}
