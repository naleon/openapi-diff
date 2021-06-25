package com.naleon.openapidiff.cli;

import com.naleon.openapidiff.cli.config.Config;
import com.naleon.openapidiff.cli.config.ServiceConfig;
import lombok.AllArgsConstructor;
import lombok.val;
import org.apache.commons.collections4.CollectionUtils;
import org.openapitools.openapidiff.core.OpenApiCompare;
import org.openapitools.openapidiff.core.model.ChangedOpenApi;
import org.openapitools.openapidiff.core.model.ChangedOperation;
import org.openapitools.openapidiff.core.model.Endpoint;
import org.openapitools.openapidiff.core.output.ConsoleRender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ApiChangeProcessor {
  static final Logger logger = LoggerFactory.getLogger(ApiChangeProcessor.class);
  private final Config config;

  public void processChances() {
    config.getServiceConfigs().stream()
        .forEach(
            serviceConfig -> {
              val changeResult =
                  OpenApiCompare.fromLocations(
                      serviceConfig.getSnapshotFile(), serviceConfig.getOpenApiUrl());
              processChangeResult(changeResult, serviceConfig);
            });
  }

  private void processChangeResult(ChangedOpenApi changeResult, ServiceConfig serviceConfig) {
    ConsoleRender consoleRender = new ConsoleRender();
    if (CollectionUtils.isEmpty(serviceConfig.getInterestedEndpoints())) {
      consoleRender.render(changeResult);
      return;
    }
    val changedOperations = filterChangedOperations(changeResult, serviceConfig);
    val missingEndPoints = filterMissingEndpoints(changeResult, serviceConfig);
    changeResult.getChangedOperations().clear();
    changeResult.getMissingEndpoints().clear();
    changeResult.getChangedOperations().addAll(changedOperations);
    changeResult.getMissingEndpoints().addAll(missingEndPoints);
    val output = consoleRender.render(changeResult);
    logger.info(String.format("\n\n%s",output));
  }

  private List<ChangedOperation> filterChangedOperations(
      ChangedOpenApi changeResult, ServiceConfig serviceConfig) {
    return changeResult.getChangedOperations().stream()
        .filter(
            changed ->
                serviceConfig.getInterestedEndpoints().stream()
                    .filter(
                        endpointConfig ->
                            endpointConfig.getEndpoint().contains(changed.getPathUrl()))
                    .findFirst()
                    .map(
                        endpointConfig ->
                            endpointConfig.getMethods().stream()
                                .filter(
                                    endpointMethod ->
                                        endpointMethod
                                            .name()
                                            .equals(changed.getHttpMethod().name()))
                                .findFirst()
                                .map(endpointMethod -> true)
                                .orElse(false))
                    .orElse(false))
        .collect(Collectors.toList());
  }

  private List<Endpoint> filterMissingEndpoints(
      ChangedOpenApi changeResult, ServiceConfig serviceConfig) {
    return changeResult.getMissingEndpoints().stream()
        .filter(
            changed ->
                serviceConfig.getInterestedEndpoints().stream()
                    .filter(
                        endpointConfig ->
                            endpointConfig.getEndpoint().contains(changed.getPathUrl()))
                    .findFirst()
                    .map(
                        endpointConfig ->
                            endpointConfig.getMethods().stream()
                                .filter(
                                    endpointMethod ->
                                        endpointMethod
                                            .name()
                                            .equals(changed.getMethod().toString()))
                                .findFirst()
                                .map(endpointMethod -> true)
                                .orElse(false))
                    .orElse(false))
        .collect(Collectors.toList());
  }
}
