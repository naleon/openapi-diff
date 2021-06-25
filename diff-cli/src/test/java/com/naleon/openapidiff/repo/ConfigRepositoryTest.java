package com.naleon.openapidiff.repo;

import com.naleon.openapidiff.cli.config.Config;
import com.naleon.openapidiff.cli.config.EndpointConfig;
import com.naleon.openapidiff.cli.config.ServiceConfig;
import lombok.val;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.UUID;

public class ConfigRepositoryTest {

  @Test
  public void saveConfig() {
    val configFile =
        FileUtils.getTempDirectoryPath() + File.separator + UUID.randomUUID().toString();
    val configRepository = new ConfigRepository(configFile);
    val config =
        Config.builder()
            .breakingChangesOnly(true)
            .serviceConfigs(
                Arrays.asList(
                    ServiceConfig.builder()
                        .apiName("some-api-name")
                        .openApiUrl("hhtp://someurl.com")
                        .interestedEndpoints(
                            Arrays.asList(
                                EndpointConfig.builder()
                                    .endpoint("/carts")
                                    .methods(Arrays.asList(EndpointConfig.EndpointMethod.GET))
                                    .build()))
                        .build(),
                    ServiceConfig.builder()
                        .apiName("some-api-name-2")
                        .openApiUrl("hhtp://someurl-2.com")
                        .interestedEndpoints(
                            Arrays.asList(
                                EndpointConfig.builder()
                                    .endpoint("/campaigns")
                                        .methods(Arrays.asList(EndpointConfig.EndpointMethod.GET))
                                    .build()))
                        .build()))
            .build();
    configRepository.saveConfig(config);
  }

  @Test
  public void getConfig() {}
}
