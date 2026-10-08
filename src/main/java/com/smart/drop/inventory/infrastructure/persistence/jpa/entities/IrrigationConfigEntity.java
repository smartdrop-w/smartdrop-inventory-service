package com.smart.drop.inventory.infrastructure.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "IrrigationConfigs")
public class IrrigationConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IrrigationConfigId")
    private Integer irrigationConfigId;

    @Column(name = "UserId", nullable = false, unique = true)
    private Integer userId;

    @Column(name = "ManualOverride", nullable = false)
    private Boolean manualOverride;

    @Column(name = "SoilMoisture")
    private Integer soilMoisture;

    @Column(name = "IrrigationNext", length = 100)
    private String irrigationNext;

    public Integer getIrrigationConfigId() {
        return irrigationConfigId;
    }

    public void setIrrigationConfigId(Integer irrigationConfigId) {
        this.irrigationConfigId = irrigationConfigId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getManualOverride() {
        return manualOverride;
    }

    public void setManualOverride(Boolean manualOverride) {
        this.manualOverride = manualOverride;
    }

    public Integer getSoilMoisture() {
        return soilMoisture;
    }

    public void setSoilMoisture(Integer soilMoisture) {
        this.soilMoisture = soilMoisture;
    }

    public String getIrrigationNext() {
        return irrigationNext;
    }

    public void setIrrigationNext(String irrigationNext) {
        this.irrigationNext = irrigationNext;
    }
}
