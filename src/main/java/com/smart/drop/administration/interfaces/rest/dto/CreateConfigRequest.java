package com.smart.drop.administration.interfaces.rest.dto;

public record CreateConfigRequest(
        String parameterKey,
        String parameterValue
) {
}

