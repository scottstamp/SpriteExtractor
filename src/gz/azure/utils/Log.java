package gz.azure.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Azure Camera Server - Furni Sprite Extractor
 * Written by Scott Stamp (scottstamp851, scott@hypermine.com)
 */
public final class Log {
    private static final Logger logger = LoggerFactory.getLogger(new Exception().getStackTrace()[1].getClassName());

    public static void println() {
        println("");
    }

    public static void println(String message) {
        System.out.println(message);
    }

    public static void info(String message) {
        info(message, null);
    }

    public static void info(String message, Throwable exception) {
        logger.info(message, exception);
    }

    public static void warn(String message) {
        warn(message, null);
    }

    public static void warn(String message, Throwable exception) {
        logger.warn(message, exception);
    }

    public static void error(String message) {
        error(message, null);
    }

    public static void error(String message, Throwable exception) {
        logger.error(message, exception);
    }
}
