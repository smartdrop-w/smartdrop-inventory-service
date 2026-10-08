package com.smart.drop.inventory.infrastructure.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Consumption")
public class ConsumptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ConsumptionId")
    private Integer consumptionId;

    @Column(name = "UserId")
    private Integer userId;

    @Column(name = "VolumeUsed", precision = 10, scale = 2)
    private BigDecimal volumeUsed;

    @Column(name = "DateRecorded")
    private LocalDateTime dateRecorded;

    @Column(name = "UsageContext", length = 50)
    private String usageContext;

    public Integer getConsumptionId() {
        return consumptionId;
    }

    public void setConsumptionId(Integer consumptionId) {
        this.consumptionId = consumptionId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getVolumeUsed() {
        return volumeUsed;
    }

    public void setVolumeUsed(BigDecimal volumeUsed) {
        this.volumeUsed = volumeUsed;
    }

    public LocalDateTime getDateRecorded() {
        return dateRecorded;
    }

    public void setDateRecorded(LocalDateTime dateRecorded) {
        this.dateRecorded = dateRecorded;
    }

    public String getUsageContext() {
        return usageContext;
    }

    public void setUsageContext(String usageContext) {
        this.usageContext = usageContext;
    }
}

