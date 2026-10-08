package com.smart.drop.inventory.interfaces.rest.dto;

import java.math.BigDecimal;

public record RegisterConsumptionRequest(
        Integer userId,
        BigDecimal volumeUsed,
        String usageContext
) {
}

