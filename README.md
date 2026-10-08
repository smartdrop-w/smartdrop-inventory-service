# smartdrop-inventory-service

> **SmartDrop â€” IoT Liquid Monitoring & Quality Management**  
> UPC â€” Fundamentos de Arquitectura de Software (2026-20)  
> Autor: **Angel Jose Pariona Chacca**

## ðŸ“‹ Descripcion
SmartDrop Inventory Microservice: Tanques, Dispositivos IoT, Consumo, Proxy Cache-Aside y Factory Method

## ðŸš€ Ejecucion Rapida (Zero Friction)
Para iniciar el servicio localmente:
``bash
# En Windows PowerShell
./mvnw spring-boot:run
``

* **Puerto Local:** $(System.Collections.Hashtable.Port)
* **Swagger UI:** [http://localhost:8082/swagger-ui/index.html](http://localhost:8082/swagger-ui/index.html)
* **OpenAPI Docs:** [http://localhost:8082/v3/api-docs](http://localhost:8082/v3/api-docs)
* **Health Check Probe:** [http://localhost:8082/api/v1/health](http://localhost:8082/api/v1/health)

## ðŸ§ª Pruebas Automatizadas
``bash
./mvnw test
``
