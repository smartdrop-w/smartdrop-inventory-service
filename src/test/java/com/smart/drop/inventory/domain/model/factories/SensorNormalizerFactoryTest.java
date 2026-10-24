package com.smart.drop.inventory.domain.model.factories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Pruebas Unitarias - Patrón Factory Method: Normalización de Sensores IoT")
class SensorNormalizerFactoryTest {

    private SensorNormalizerFactory factory;

    @BeforeEach
    void setUp() {
        List<ISensorDataNormalizer> normalizers = List.of(
                new UltrasonicSensorNormalizer(),
                new ThermalSensorNormalizer()
        );
        factory = new SensorNormalizerFactory(normalizers);
    }

    @Test
    @DisplayName("Debe crear UltrasonicSensorNormalizer y calcular porcentaje de llenado correctamente")
    void shouldCreateUltrasonicNormalizerAndComputeLevel() {
        // Arrange: Sensor a 40 cm del líquido en tanque de 200 cm -> 160 cm de agua (80.0% de llenado)
        RawSensorPayload raw = new RawSensorPayload("DEV-US-001", "ULTRASONIC", 40.0, "CM", System.currentTimeMillis());

        // Act
        ISensorDataNormalizer normalizer = factory.createNormalizer(SensorType.ULTRASONIC_LEVEL);
        NormalizedTelemetryReading reading = normalizer.normalize(raw);

        // Assert
        assertThat(normalizer).isInstanceOf(UltrasonicSensorNormalizer.class);
        assertThat(reading.sensorId()).isEqualTo("DEV-US-001");
        assertThat(reading.sensorType()).isEqualTo(SensorType.ULTRASONIC_LEVEL);
        assertThat(reading.normalizedValue()).isEqualTo(80.0);
        assertThat(reading.canonicalUnit()).isEqualTo("PERCENTAGE");
        assertThat(reading.metricName()).isEqualTo("TANK_FILL_LEVEL");
    }

    @Test
    @DisplayName("Debe normalizar lecturas de sondas térmicas en Fahrenheit a Celsius canónico")
    void shouldCreateThermalNormalizerAndConvertFahrenheitToCelsius() {
        // Arrange: 68.0 °F -> 20.0 °C
        RawSensorPayload raw = new RawSensorPayload("DEV-TH-002", "THERMAL", 68.0, "FAHRENHEIT", System.currentTimeMillis());

        // Act
        NormalizedTelemetryReading reading = factory.normalizeReading(SensorType.THERMAL_PROBE, raw);

        // Assert
        assertThat(reading.sensorId()).isEqualTo("DEV-TH-002");
        assertThat(reading.sensorType()).isEqualTo(SensorType.THERMAL_PROBE);
        assertThat(reading.normalizedValue()).isEqualTo(20.0);
        assertThat(reading.canonicalUnit()).isEqualTo("CELSIUS");
        assertThat(reading.metricName()).isEqualTo("FERMENTATION_TEMPERATURE");
    }
}
