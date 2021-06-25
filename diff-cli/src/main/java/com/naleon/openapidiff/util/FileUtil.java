package com.naleon.openapidiff.util;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static com.naleon.openapidiff.util.ErrorUtil.exitWithError;

public class FileUtil {
  private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

  public static void writeFile(String output, String outputFile) {
    File file = new File(outputFile);
    try {
      Files.deleteIfExists(file.toPath());
      FileUtils.writeStringToFile(file, output, StandardCharsets.UTF_8);
    } catch (IOException e) {
      ErrorUtil.exitWithError(String.format("Impossible to write output to file %s", outputFile), e, logger);
    }
  }
}
