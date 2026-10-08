package com.smart.drop.inventory.infrastructure.persistence.proxy;

import com.smart.drop.inventory.infrastructure.persistence.jpa.entities.TankEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - Patrón Proxy (Cache-Aside): Optimización de Consultas")
class CachedTankReadServiceProxyTest {

    @Mock
    private TankReadService realService;

    private CachedTankReadServiceProxy proxy;

    @BeforeEach
    void setUp() {
        proxy = new CachedTankReadServiceProxy(realService);
    }

    @Test
    @DisplayName("Primera llamada debe consultar al servicio real (Cache Miss) y almacenar en memoria")
    void firstCallShouldDelegateToRealServiceAndPopulateCache() {
        // Arrange
        Integer tankId = 101;
        TankEntity mockTank = new TankEntity();
        mockTank.setTankId(tankId);
        mockTank.setName("Cisterna Principal");
        mockTank.setCapacity(1000);
        mockTank.setCurrent(800);

        when(realService.findTankById(tankId)).thenReturn(Optional.of(mockTank));

        // Act
        Optional<TankEntity> result = proxy.findTankById(tankId);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Cisterna Principal");
        assertThat(proxy.getMissCount()).isEqualTo(1);
        assertThat(proxy.getHitCount()).isEqualTo(0);
        verify(realService, times(1)).findTankById(tankId);
    }

    @Test
    @DisplayName("Segunda llamada idéntica debe responder desde la caché en memoria (Cache Hit) con 0 consultas al servicio real")
    void subsequentCallsShouldHitCacheWithoutCallingRealService() {
        // Arrange
        Integer tankId = 202;
        TankEntity mockTank = new TankEntity();
        mockTank.setTankId(tankId);
        mockTank.setName("Fermentador Cónico");

        when(realService.findTankById(tankId)).thenReturn(Optional.of(mockTank));

        // Act: Primera llamada (Miss)
        proxy.findTankById(tankId);
        // Act: Segunda y tercera llamada (Hit)
        Optional<TankEntity> secondCall = proxy.findTankById(tankId);
        Optional<TankEntity> thirdCall = proxy.findTankById(tankId);

        // Assert
        assertThat(secondCall).isPresent();
        assertThat(thirdCall).isPresent();
        assertThat(proxy.getMissCount()).isEqualTo(1);
        assertThat(proxy.getHitCount()).isEqualTo(2);
        // Verificamos que el servicio real solo fue invocado UNA sola vez
        verify(realService, times(1)).findTankById(tankId);
    }

    @Test
    @DisplayName("Al desalojar la caché (evictCache), la siguiente consulta debe volver a delegar al servicio real")
    void evictCacheShouldForceSubsequentLookupOnRealService() {
        // Arrange
        Integer tankId = 303;
        TankEntity mockTank = new TankEntity();
        mockTank.setTankId(tankId);

        when(realService.findTankById(tankId)).thenReturn(Optional.of(mockTank));

        // Act
        proxy.findTankById(tankId); // Miss 1
        assertThat(proxy.getCachedTanksCount()).isEqualTo(1);

        proxy.evictCache(tankId);   // Invalidación
        assertThat(proxy.getCachedTanksCount()).isEqualTo(0);

        proxy.findTankById(tankId); // Miss 2

        // Assert
        assertThat(proxy.getMissCount()).isEqualTo(2);
        verify(realService, times(2)).findTankById(tankId);
    }
}
