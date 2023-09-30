package KAGO_framework.Core.Debug;

import KAGO_framework.Config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Debug {
    private static final String ANSI_DEFAULT = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    private static final String ANSI_BLACK = "\u001B[30m";

    public static void Log(String message, LogType logType){
        StringBuilder messageBuilder = new StringBuilder(message);

        // Get caller class name
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String callerClassName = stackTrace[2].getClassName();

        // Get current time
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String currentTime = currentDateTime.format(dateTimeFormatter);

        // Insert "pre message"
        String preMessage = String.format("[%s][%s]: ", callerClassName, currentTime);
        messageBuilder.insert(0, preMessage);

        switch (logType) {
            case LOG, INFO -> logInfo(messageBuilder.toString());

            case SUCCESS -> logDebug(messageBuilder.toString(), ANSI_GREEN);
            case WARNING -> logDebug(messageBuilder.toString(), ANSI_YELLOW);
            case ERROR -> logDebug(messageBuilder.toString(), ANSI_RED);
            case FATAL -> logDebug(messageBuilder.toString(), ANSI_RED_BACKGROUND + ANSI_BLACK);
        }
    }

    private static void logInfo(String message) {
        if (!Config.LOG_AND_INFO_MESSAGES)
            return;

        System.out.println(ANSI_DEFAULT + message);
    }

    private static void logDebug(String message, String ansiColor) {
        if (!Config.DEBUG_MESSAGES)
            return;

        System.out.println(ansiColor + message + ANSI_DEFAULT);
    }
}
