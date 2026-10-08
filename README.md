# smartdrop-inventory-service

> **SmartDrop - IoT Liquid Monitoring & Quality Management**  
> *UPC - Fundamentos de Arquitectura de Software (2026-20)*  
> *Autor Responsable:* **Angel Jose Pariona Chacca**

---

## Descripcion General

Microservicio encargado del inventario de tanques, telemetria de dispositivos sensores IoT, registro historico de consumo, implementacion del patron Proxy Cache-Aside y Factory Method para normalizacion de sensores.

---

## Ejecucion en Entorno Local

Para compilar y ejecutar el proyecto localmente sin preconfiguraciones externas:

```powershell
# Compilacion y arranque con Maven Wrapper
./mvnw spring-boot:run
```

## Configuracion de Puertos y Endpoints

* **Puerto Local:** 8082
* **Swagger UI:** [http://localhost:8082/swagger-ui/index.html](http://localhost:8082/swagger-ui/index.html)
* **OpenAPI Especificacion JSON:** [http://localhost:8082/v3/api-docs](http://localhost:8082/v3/api-docs)
* **Health Check Liveness Probe:** [http://localhost:8082/api/v1/health](http://localhost:8082/api/v1/health)

---

## Pruebas Automatizadas

Para validar la suite de pruebas unitarias y de integracion:

```powershell
./mvnw test
```