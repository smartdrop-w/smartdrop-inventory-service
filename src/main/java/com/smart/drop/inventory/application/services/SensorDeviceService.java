package com.smart.drop.inventory.application.services;

import com.smart.drop.inventory.domain.model.entities.Consumption;
import com.smart.drop.inventory.domain.model.entities.SensorDevice;
import com.smart.drop.inventory.domain.model.entities.Tank;
import com.smart.drop.inventory.domain.model.factories.NormalizedTelemetryReading;
import com.smart.drop.inventory.domain.model.factories.RawSensorPayload;
import com.smart.drop.inventory.domain.model.factories.SensorNormalizerFactory;
import com.smart.drop.inventory.domain.model.repositories.SensorDeviceRepository;
import com.smart.drop.inventory.interfaces.rest.dto.TelemetryIngestionRequest;
import com.smart.drop.inventory.interfaces.rest.dto.TelemetryIngestionResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SensorDeviceService {

    private final SensorDeviceRepository sensorDeviceRepository;
    private final SensorNormalizerFactory sensorNormalizerFactory;
    private final TankService tankService;
    private final ConsumptionService consumptionService;

    public SensorDeviceService(SensorDeviceRepository sensorDeviceRepository,
                               SensorNormalizerFactory sensorNormalizerFactory,
                               TankService tankService,
                               ConsumptionService consumptionService) {
        this.sensorDeviceRepository = sensorDeviceRepository;
        this.sensorNormalizerFactory = sensorNormalizerFactory;
        this.tankService = tankService;
        this.consumptionService = consumptionService;
    }

    public SensorDevice createDevice(Integer userId, String name, String location, String flow, String daily, Integer battery, String status) {
        SensorDevice device = SensorDevice.create(userId, name, location, flow, daily, battery, status);
        return sensorDeviceRepository.save(device);
    }

    @Transactional(readOnly = true)
    public Optional<SensorDevice> getById(Integer deviceId) {
        return sensorDeviceRepository.findById(deviceId);
    }

    @Transactional(readOnly = true)
    public List<SensorDevice> getByUserId(Integer userId) {
        return sensorDeviceRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<SensorDevice> getAll() {
        return sensorDeviceRepository.findAll();
    }

    public SensorDevice updateDevice(Integer deviceId, String name, String location, String flow, String daily, Integer battery, String status) {
        SensorDevice current = sensorDeviceRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("SensorDevice not found: " + deviceId));

        SensorDevice updated = new SensorDevice(
                current.deviceId(),
                current.userId(),
                name,
                location,
                flow,
                daily,
                battery,
                status
        );
        return sensorDeviceRepository.save(updated);
    }

    public TelemetryIngestionResponse processTelemetry(TelemetryIngestionRequest request) {
        RawSensorPayload raw = new RawSensorPayload(
                request.sensorId(),
                request.rawType(),
                request.rawValue(),
                request.unit(),
                request.epochTimestamp() > 0 ? request.epochTimestamp() : System.currentTimeMillis()
        );

        NormalizedTelemetryReading normalized = sensorNormalizerFactory.normalizeReading(request.sensorType(), raw);

        Integer updatedTankId = null;
        Integer updatedTankLevel = null;
        Integer consumptionId = null;

        // Actualización de tanque si se especificó tankId
        if (request.tankId() != null) {
            Optional<Tank> optTank = tankService.getById(request.tankId());
            if (optTank.isPresent()) {
                Tank tank = optTank.get();
                // Si el valor normalizado es porcentaje (0-100), actualizamos el nivel en litros
                int newLevel = (int) Math.round(tank.capacity() * (normalized.normalizedValue() / 100.0));
                newLevel = Math.max(0, Math.min(tank.capacity(), newLevel));
                Tank updated = tankService.updateTank(tank.tankId(), tank.name(), tank.capacity(), newLevel);
                updatedTankId = updated.tankId();
                updatedTankLevel = updated.current();
            }
        }

        // Registro de consumo proporcional si el sensor genera telemetría de flujo o nivel
        if (request.userId() != null && ("PERCENTAGE".equalsIgnoreCase(normalized.canonicalUnit()) || "LITERS".equalsIgnoreCase(normalized.canonicalUnit()))) {
            Consumption c = consumptionService.registerConsumption(
                    request.userId(),
                    BigDecimal.valueOf(Math.max(1.0, normalized.normalizedValue() * 0.1)),
                    "Telemetry Ingestion: " + normalized.metricName()
            );
            consumptionId = c.consumptionId();
        }

        // Actualización de estado del dispositivo si existe
        if (request.deviceId() != null) {
            sensorDeviceRepository.findById(request.deviceId()).ifPresent(dev -> {
                SensorDevice updated = new SensorDevice(
                        dev.deviceId(),
                        dev.userId(),
                        dev.name(),
                        dev.location(),
                        normalized.normalizedValue() + " " + normalized.canonicalUnit(),
                        dev.daily(),
                        dev.battery(),
                        "ONLINE"
                );
                sensorDeviceRepository.save(updated);
            });
        }

        return new TelemetryIngestionResponse(
                true,
                normalized.sensorId(),
                normalized.sensorType().name(),
                normalized.normalizedValue(),
                normalized.canonicalUnit(),
                normalized.metricName(),
                updatedTankId,
                updatedTankLevel,
                consumptionId,
                "Telemetry successfully normalized via SensorNormalizerFactory and recorded.",
                LocalDateTime.now()
        );
    }

    public void deleteById(Integer deviceId) {
        sensorDeviceRepository.deleteById(deviceId);
    }
}
