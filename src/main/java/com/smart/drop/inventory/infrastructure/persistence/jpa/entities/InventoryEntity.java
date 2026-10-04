package com.smart.drop.inventory.infrastructure.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Inventory")
public class InventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "InventoryId")
    private Integer inventoryId;

    @Column(name = "UserId")
    private Integer userId;

    @Column(name = "AvailableLiters", precision = 10, scale = 2)
    private BigDecimal availableLiters;

    @Column(name = "LastRefillDate")
    private LocalDate lastRefillDate;

    @Column(name = "NextRefillPrediction")
    private LocalDate nextRefillPrediction;

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getAvailableLiters() {
        return availableLiters;
    }

    public void setAvailableLiters(BigDecimal availableLiters) {
        this.availableLiters = availableLiters;
    }

    public LocalDate getLastRefillDate() {
        return lastRefillDate;
    }

    public void setLastRefillDate(LocalDate lastRefillDate) {
        this.lastRefillDate = lastRefillDate;
    }

    public LocalDate getNextRefillPrediction() {
        return nextRefillPrediction;
    }

    public void setNextRefillPrediction(LocalDate nextRefillPrediction) {
        this.nextRefillPrediction = nextRefillPrediction;
    }
}

