package com.naleon.openapidiff.cli;

import static com.naleon.openapidiff.util.ErrorUtil.exitWithError;

import com.naleon.openapidiff.cli.config.Config;
import com.naleon.openapidiff.cli.config.ServiceConfig;
import com.naleon.openapidiff.repo.ConfigRepository;
import com.naleon.openapidiff.util.FileUtil;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import java.io.File;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.val;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class SnapshotHandler {
  private static final Logger logger = LoggerFactory.getLogger(SnapshotHandler.class);
  private final ConfigRepository configRepository;
  private final OkHttpClient client = new OkHttpClient();
  private final Config config;

  public void generateSnapshot() {
    val serviceConfigs = config.getServiceConfigs();
    if (CollectionUtils.isEmpty(serviceConfigs)) {
      exitWithError("There is no service config to snapshot", logger);
    }
    val snapshotDirectory = resolveSnapshotDirectory();
    serviceConfigs.stream()
        .forEach(serviceConfig -> takeSnapshot(serviceConfig, snapshotDirectory));
    configRepository.saveConfig(config);
    logger.info("snapshot saved successfully");
  }

  private void takeSnapshot(ServiceConfig serviceConfig, String snapshotDirectory) {
    val request = new Request.Builder().url(serviceConfig.getOpenApiUrl()).build();
    try {
      val response = client.newCall(request).execute();
      val name = serviceConfig.getApiName();
      if (!response.isSuccessful()) {
        exitWithError(
            String.format(
                "Error calling endpoint %s: %s",
                request.httpUrl().toString(), response.body().string()),
            logger);
      }
      val snapshotFilePath = String.format("%s%s.json", snapshotDirectory, name);
      FileUtil.writeFile(response.body().string(), snapshotFilePath);
      serviceConfig.setSnapshotFile(snapshotFilePath);
    } catch (IOException e) {
      exitWithError(
          String.format(
              "unexpected error during snapshot requesting %s", request.httpUrl().toString()),
          e,
          logger);
    }
  }

  private String resolveSnapshotDirectory() {
    val snapshotDirectory = config.getSnapshotsDirectory();
    if (StringUtils.isEmpty(config.getSnapshotsDirectory())) {
      exitWithError("Snapshot directory must be configured", logger);
    }
    if (!snapshotDirectory.endsWith(File.separator)) {
      return String.format("%s%s", snapshotDirectory, File.separator);
    }
    return snapshotDirectory;
  }
}
