package com.smart.drop.inventory.interfaces.rest.dto;

public record TankResponse(
        Integer tankId,
        Integer userId,
        String name,
        Integer capacity,
        Integer current
) {
}
