package com.smart.drop.inventory.interfaces.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateInventoryRequest(
        Integer userId,
        BigDecimal availableLiters,
        LocalDate lastRefillDate,
        LocalDate nextRefillPrediction
) {
}

