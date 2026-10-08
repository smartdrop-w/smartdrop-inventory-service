package com.smart.drop.inventory.config;

import com.smart.drop.inventory.infrastructure.persistence.jpa.entities.SensorDeviceEntity;
import com.smart.drop.inventory.infrastructure.persistence.jpa.entities.TankEntity;
import com.smart.drop.inventory.infrastructure.persistence.jpa.repositories.SensorDeviceJpaRepository;
import com.smart.drop.inventory.infrastructure.persistence.jpa.repositories.TankJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InventoryDataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(InventoryDataInitializer.class);

    private final TankJpaRepository tankRepository;
    private final SensorDeviceJpaRepository deviceRepository;

    public InventoryDataInitializer(TankJpaRepository tankRepository, SensorDeviceJpaRepository deviceRepository) {
        this.tankRepository = tankRepository;
        this.deviceRepository = deviceRepository;
    }

    @Override
    public void run(String... args) {
        if (tankRepository.count() > 0) {
            log.info("[Inventory Service] Semillero omitido: Ya existen tanques registrados.");
            return;
        }

        log.info("[Inventory Service] Sembrando tanques y sensores de prueba...");

        // Tanques (userId: 1 = Residencial, 2 = Cervecero)
        TankEntity tank1 = new TankEntity();
        tank1.setUserId(1);
        tank1.setName("Cisterna Principal Residencial");
        tank1.setCapacity(1000);
        tank1.setCurrent(820);
        tankRepository.save(tank1);

        TankEntity tank2 = new TankEntity();
        tank2.setUserId(2);
        tank2.setName("Fermentador Cónico Ale #1");
        tank2.setCapacity(500);
        tank2.setCurrent(480);
        tankRepository.save(tank2);

        TankEntity tank3 = new TankEntity();
        tank3.setUserId(2);
        tank3.setName("Tanque de Agua Helada (HLT)");
        tank3.setCapacity(1200);
        tank3.setCurrent(950);
        tankRepository.save(tank3);

        // Dispositivos Sensores
        SensorDeviceEntity device1 = new SensorDeviceEntity();
        device1.setUserId(1);
        device1.setName("Sensor Ultrasónico HC-SR04 Cisterna");
        device1.setLocation("Techo / Cisterna Residencial");
        device1.setFlow("0.0 L/min");
        device1.setDaily("180 L");
        device1.setBattery(94);
        device1.setStatus("ONLINE");
        deviceRepository.save(device1);

        SensorDeviceEntity device2 = new SensorDeviceEntity();
        device2.setUserId(2);
        device2.setName("Sonda Térmica Digital PT100 Fermentador");
        device2.setLocation("Planta Fermentación Bohórquez");
        device2.setFlow("1.2 L/min");
        device2.setDaily("480 L");
        device2.setBattery(100);
        device2.setStatus("ONLINE");
        deviceRepository.save(device2);

        log.info("[Inventory Service] Sembrado completado: 3 tanques y 2 dispositivos creados.");
    }
}
