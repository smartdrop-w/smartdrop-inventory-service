package com.smart.drop.inventory.application.services;

import com.smart.drop.inventory.domain.model.entities.Consumption;
import com.smart.drop.inventory.domain.model.entities.SensorDevice;
import com.smart.drop.inventory.domain.model.entities.Tank;
import com.smart.drop.inventory.domain.model.factories.SensorNormalizerFactory;
import com.smart.drop.inventory.domain.model.factories.SensorType;
import com.smart.drop.inventory.domain.model.factories.ThermalSensorNormalizer;
import com.smart.drop.inventory.domain.model.factories.UltrasonicSensorNormalizer;
import com.smart.drop.inventory.domain.model.repositories.SensorDeviceRepository;
import com.smart.drop.inventory.domain.model.repositories.TankRepository;
import com.smart.drop.inventory.interfaces.rest.dto.TankStatusResponse;
import com.smart.drop.inventory.interfaces.rest.dto.TelemetryIngestionRequest;
import com.smart.drop.inventory.interfaces.rest.dto.TelemetryIngestionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - Estado de Tanques e Ingesta de Telemetría Normalizada")
class TankStatusAndTelemetryTest {

    @Mock
    private TankRepository tankRepository;

    @Mock
    private SensorDeviceRepository sensorDeviceRepository;

    @Mock
    private ConsumptionService consumptionService;

    private TankService tankService;
    private SensorNormalizerFactory factory;
    private SensorDeviceService sensorDeviceService;

    @BeforeEach
    void setUp() {
        tankService = new TankService(tankRepository);
        factory = new SensorNormalizerFactory(List.of(
                new UltrasonicSensorNormalizer(),
                new ThermalSensorNormalizer()
        ));
        sensorDeviceService = new SensorDeviceService(sensorDeviceRepository, factory, tankService, consumptionService);
    }

    @Test
    @DisplayName("Debe calcular correctamente el porcentaje y alertar cuando el tanque tiene nivel bajo")
    void shouldCalculateTankStatusAndTriggerLowAlert() {
        // Arrange: Tanque de 2000L con 400L (20.0% -> LOW alert)
        Tank tank = new Tank(1, 2, "Tanque Reserva", 2000, 400);
        when(tankRepository.findById(1)).thenReturn(Optional.of(tank));

        // Act
        TankStatusResponse status = tankService.getTankStatus(1);

        // Assert
        assertThat(status.tankId()).isEqualTo(1);
        assertThat(status.capacity()).isEqualTo(2000);
        assertThat(status.currentLevel()).isEqualTo(400);
        assertThat(status.percentage()).isEqualTo(20.0);
        assertThat(status.status()).isEqualTo("LOW");
        assertThat(status.isLowLevelAlert()).isTrue();
        assertThat(status.isCriticalAlert()).isFalse();
    }

    @Test
    @DisplayName("Debe alertar CRITICAL cuando el tanque tiene menos de 15% de agua")
    void shouldCalculateTankStatusAndTriggerCriticalAlert() {
        // Arrange: Tanque de 2000L con 100L (5.0% -> CRITICAL)
        Tank tank = new Tank(2, 2, "Tanque Auxiliar", 2000, 100);
        when(tankRepository.findById(2)).thenReturn(Optional.of(tank));

        // Act
        TankStatusResponse status = tankService.getTankStatus(2);

        // Assert
        assertThat(status.percentage()).isEqualTo(5.0);
        assertThat(status.status()).isEqualTo("CRITICAL");
        assertThat(status.isCriticalAlert()).isTrue();
    }

    @Test
    @DisplayName("Debe procesar telemetría ultrasónica, normalizar nivel y actualizar tanque y consumo")
    void shouldProcessUltrasonicTelemetryAndSyncTank() {
        // Arrange: Sensor a 40cm -> 80.0% nivel. Tanque de 1000L -> nuevo nivel 800L
        Tank tank = new Tank(1, 2, "Tanque Principal", 1000, 500);
        when(tankRepository.findById(1)).thenReturn(Optional.of(tank));
        when(tankRepository.save(any(Tank.class))).thenAnswer(inv -> inv.getArgument(0));

        Consumption mockConsumption = new Consumption(10, 2, BigDecimal.valueOf(8.0), java.time.LocalDateTime.now(), "Telemetry Ingestion");
        when(consumptionService.registerConsumption(any(), any(), any())).thenReturn(mockConsumption);

        TelemetryIngestionRequest request = new TelemetryIngestionRequest(
                1,
                2,
                1,
                SensorType.ULTRASONIC_LEVEL,
                "DEV-US-001",
                "ULTRASONIC",
                40.0,
                "CM",
                System.currentTimeMillis()
        );

        // Act
        TelemetryIngestionResponse response = sensorDeviceService.processTelemetry(request);

        // Assert
        assertThat(response.processed()).isTrue();
        assertThat(response.normalizedValue()).isEqualTo(80.0);
        assertThat(response.canonicalUnit()).isEqualTo("PERCENTAGE");
        assertThat(response.updatedTankId()).isEqualTo(1);
        assertThat(response.updatedTankLevel()).isEqualTo(800);
        assertThat(response.registeredConsumptionId()).isEqualTo(10);
        verify(consumptionService, times(1)).registerConsumption(eq(2), any(), any());
    }
}
