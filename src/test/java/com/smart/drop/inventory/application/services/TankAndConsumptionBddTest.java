package com.smart.drop.inventory.application.services;

import com.smart.drop.inventory.domain.model.entities.Consumption;
import com.smart.drop.inventory.domain.model.entities.Tank;
import com.smart.drop.inventory.domain.model.repositories.ConsumptionRepository;
import com.smart.drop.inventory.domain.model.repositories.TankRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas BDD y Unitarias - Microservicio Inventory: Tanques y Consumo de Agua")
class TankAndConsumptionBddTest {

    @Mock
    private TankRepository tankRepository;

    @Mock
    private ConsumptionRepository consumptionRepository;

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private TankService tankService;

    @InjectMocks
    private ConsumptionService consumptionService;

    @Test
    @DisplayName("US01 Escenario 1: Registro exitoso de un tanque de almacenamiento con capacidad valida")
    void shouldCreateTankWithValidCapacity() {
        // Given
        when(tankRepository.save(any(Tank.class))).thenAnswer(inv -> {
            Tank t = inv.getArgument(0);
            return new Tank(501, t.userId(), t.name(), t.capacity(), t.current());
        });

        // When
        Tank created = tankService.createTank(1001, "Tanque Principal 1", 5000, 4200);

        // Then
        assertThat(created.tankId()).isEqualTo(501);
        assertThat(created.capacity()).isEqualTo(5000);
        assertThat(created.current()).isEqualTo(4200);
        verify(tankRepository, times(1)).save(any(Tank.class));
    }

    @Test
    @DisplayName("US01 Escenario 2: Rechazo de tanque cuando la capacidad es negativa o cero")
    void shouldRejectTankWithInvalidCapacity() {
        // When & Then
        assertThatThrownBy(() -> tankService.createTank(1001, "Tanque Invalido", -500, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Capacity must be greater than 0");
    }

    @Test
    @DisplayName("US01 Escenario 3: Reabastecimiento completo (replenish) de tanque al 100% de capacidad")
    void shouldReplenishTankToFullCapacity() {
        // Given
        Tank existing = new Tank(501, 1001, "Cisterna Principal", 5000, 1200);
        when(tankRepository.findById(501)).thenReturn(Optional.of(existing));
        when(tankRepository.save(any(Tank.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        Tank replenished = tankService.replenish(501);

        // Then
        assertThat(replenished.current()).isEqualTo(5000);
        verify(tankRepository, times(1)).save(any(Tank.class));
    }

    @Test
    @DisplayName("US02 Escenario 1: Registro de consumo de agua y descuento atomico de inventario")
    void shouldRegisterConsumptionAndSubtractFromInventory() {
        // Given
        BigDecimal volume = new BigDecimal("250.00");
        String context = "Lavado de fermentador conico";
        when(consumptionRepository.save(any(Consumption.class))).thenAnswer(inv -> {
            Consumption c = inv.getArgument(0);
            return new Consumption(901, c.userId(), c.volumeUsed(), LocalDateTime.now(), c.usageContext());
        });

        // When
        Consumption saved = consumptionService.registerConsumption(1001, volume, context);

        // Then
        assertThat(saved.consumptionId()).isEqualTo(901);
        assertThat(saved.volumeUsed()).isEqualByComparingTo("250.00");
        verify(inventoryService, times(1)).subtractConsumption(1001, volume);
    }

    @Test
    @DisplayName("US02 Escenario 2: Rechazo de consumo cuando el contexto excede los 50 caracteres")
    void shouldRejectConsumptionExceedingMaxContextLength() {
        // Given
        String longContext = "Contexto demasiado largo que supera ampliamente el limite permitido de cincuenta caracteres";

        // When & Then
        assertThatThrownBy(() -> consumptionService.registerConsumption(1001, new BigDecimal("50"), longContext))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("usageContext must be <= 50 characters");
    }
}
