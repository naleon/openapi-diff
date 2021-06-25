package com.naleon.openapidiff.cli.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndpointConfig {
  private String endpoint;
  private List<EndpointMethod> methods;

  public enum EndpointMethod {
    GET,
    POST,
    PUT,
    DELETE,
    PATCH
  }
}
