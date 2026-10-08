package com.smart.drop.inventory.interfaces.rest.dto;

public record CreateTankRequest(
        Integer userId,
        String name,
        Integer capacity,
        Integer current
) {
}
