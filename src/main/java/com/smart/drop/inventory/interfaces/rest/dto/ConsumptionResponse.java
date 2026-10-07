package com.smart.drop.inventory.interfaces.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ConsumptionResponse(
        Integer consumptionId,
        Integer userId,
        BigDecimal volumeUsed,
        LocalDateTime dateRecorded,
        String usageContext
) {
}

