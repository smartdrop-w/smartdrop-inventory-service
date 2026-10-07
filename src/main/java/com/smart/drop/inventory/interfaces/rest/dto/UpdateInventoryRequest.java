package com.smart.drop.inventory.interfaces.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateInventoryRequest(
        BigDecimal availableLiters,
        LocalDate lastRefillDate,
        LocalDate nextRefillPrediction
) {
}

