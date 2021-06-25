package com.naleon.openapidiff.repo;

import com.naleon.openapidiff.cli.config.Config;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static com.naleon.openapidiff.util.ErrorUtil.exitWithError;

@AllArgsConstructor
public class ConfigRepository {
  private static final Logger logger = LoggerFactory.getLogger(ConfigRepository.class);
  private final String configFile;
  private final ObjectMapper mapper =
      new ObjectMapper(new YAMLFactory())
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  public Config getConfig() {
    Config config = null;
    try {
      config = mapper.readValue(new File(configFile), Config.class);
    } catch (IOException e) {
      exitWithError("error to get the config", e, logger);
    }
    return config;
  }

  public void saveConfig(Config config) {
    try {
      mapper.writeValue(new File(configFile), config);
    } catch (IOException e) {
      exitWithError("error to save config", e, logger);
    }
  }
}
