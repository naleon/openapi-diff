package com.naleon.openapidiff.util;

import java.io.IOException;
import org.slf4j.Logger;

public class ErrorUtil {

  public static void exitWithError(String message, IOException e, Logger logger) {
    logger.error(message, e);
    System.exit(2);
  }

  public static void exitWithError(String message, Logger logger) {
    logger.error(message);
    System.exit(2);
  }
}
