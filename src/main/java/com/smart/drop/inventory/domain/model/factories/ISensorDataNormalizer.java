package com.smart.drop.inventory.domain.model.factories;

/**
 * Contrato de producto para los normalizadores creados por el Factory Method.
 */
public interface ISensorDataNormalizer {

    /**
     * Transforma un payload crudo en una lectura normalizada con unidades canónicas.
     */
    NormalizedTelemetryReading normalize(RawSensorPayload rawPayload);

    /**
     * Tipo de sensor que procesa esta implementación concreta.
     */
    SensorType getSupportedType();
}
