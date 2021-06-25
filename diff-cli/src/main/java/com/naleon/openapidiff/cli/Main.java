package com.naleon.openapidiff.cli;

import com.naleon.openapidiff.repo.ConfigRepository;
import lombok.val;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

  static final Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String... args) {
    Options options = new Options();
    options.addOption(Option.builder("h").longOpt("help").desc("print this message").build());
    options.addOption(
        Option.builder("c").longOpt("config").desc("config file").required(true).hasArg().build());
    options.addOption(Option.builder("s").longOpt("snapshot").desc("create snapshot").build());

    val parser = new DefaultParser();
    try {
      CommandLine line = parser.parse(options, args);
      if (line.hasOption("h")) {
        printHelp(options);
        System.exit(0);
      }

      if (!line.hasOption("c")) {
        throw new ParseException("Config file is required");
      }
      val configRepository = new ConfigRepository(line.getOptionValue("c"));
      val config = configRepository.getConfig();
      if (line.hasOption("s")) {
        logger.info("creating snapshot");
        val snapshotHandler = new SnapshotHandler(configRepository, config);
        snapshotHandler.generateSnapshot();
        System.exit(0);
      }
      val apiChangeProcessor =  new ApiChangeProcessor(config);
      apiChangeProcessor.processChances();

    } catch (ParseException e) {
      System.err.println("Parsing failed. Reason: " + e.getMessage());
      printHelp(options);
      System.exit(2);
    } catch (Exception e) {
      System.err.println(
          "Unexpected exception. Reason: "
              + e.getMessage()
              + "\n"
              + ExceptionUtils.getStackTrace(e));
      System.exit(2);
    }
  }
  private static void printHelp(Options options) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("openapi-diff config-file", options);
  }
}
