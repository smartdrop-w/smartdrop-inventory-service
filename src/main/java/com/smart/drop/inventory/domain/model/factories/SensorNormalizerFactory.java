package com.smart.drop.inventory.domain.model.factories;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Creador / Fábrica del Patrón Factory Method (GoF).
 * Permite desacoplar el origen de datos de los sensores de los algoritmos de normalización.
 */
@Service
public class SensorNormalizerFactory {

    private final Map<SensorType, ISensorDataNormalizer> normalizers;

    public SensorNormalizerFactory(List<ISensorDataNormalizer> normalizerList) {
        this.normalizers = normalizerList.stream()
                .collect(Collectors.toMap(
                        ISensorDataNormalizer::getSupportedType,
                        Function.identity()
                ));
    }

    /**
     * Factory Method: Retorna la instancia de normalizador especializada según el tipo de sensor.
     *
     * @param sensorType Tipo de sensor solicitado.
     * @return Implementación concreta de ISensorDataNormalizer.
     */
    public ISensorDataNormalizer createNormalizer(SensorType sensorType) {
        ISensorDataNormalizer normalizer = normalizers.get(sensorType);
        if (normalizer == null) {
            throw new IllegalArgumentException("No existe un normalizador registrado para el tipo de sensor: " + sensorType);
        }
        return normalizer;
    }

    /**
     * Método de conveniencia que resuelve y normaliza en un solo paso.
     */
    public NormalizedTelemetryReading normalizeReading(SensorType sensorType, RawSensorPayload rawPayload) {
        return createNormalizer(sensorType).normalize(rawPayload);
    }
}
