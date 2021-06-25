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
public class Config {
  private boolean breakingChangesOnly;
  private List<ServiceConfig> serviceConfigs;
  private String snapshotsDirectory;
}
