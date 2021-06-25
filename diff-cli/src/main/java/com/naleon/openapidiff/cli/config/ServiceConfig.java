package com.naleon.openapidiff.cli.config;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceConfig {
  private String apiName;
  private String openApiUrl;
  private String snapshotFile;
  private List<EndpointConfig> interestedEndpoints;
}
