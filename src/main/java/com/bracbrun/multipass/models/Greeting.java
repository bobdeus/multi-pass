package com.bracbrun.multipass.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Greeting(long id, String content) {
}
