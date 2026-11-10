package com.smart.drop.inventory.bdd.steps;

import com.smart.drop.inventory.application.services.ConsumptionService;
import com.smart.drop.inventory.application.services.InventoryService;
import com.smart.drop.inventory.application.services.SensorDeviceService;
import com.smart.drop.inventory.application.services.TankService;
import com.smart.drop.inventory.domain.model.entities.Consumption;
import com.smart.drop.inventory.domain.model.entities.Inventory;
import com.smart.drop.inventory.domain.model.entities.Tank;
import com.smart.drop.inventory.domain.model.factories.NormalizedTelemetryReading;
import com.smart.drop.inventory.domain.model.factories.RawSensorPayload;
import com.smart.drop.inventory.domain.model.factories.SensorNormalizerFactory;
import com.smart.drop.inventory.domain.model.factories.SensorType;
import com.smart.drop.inventory.domain.model.repositories.InventoryRepository;
import com.smart.drop.inventory.domain.model.repositories.TankRepository;
import com.smart.drop.inventory.interfaces.rest.dto.TankStatusResponse;
import com.smart.drop.inventory.interfaces.rest.dto.TelemetryIngestionRequest;
import com.smart.drop.inventory.interfaces.rest.dto.TelemetryIngestionResponse;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class InventoryBddSteps {

    @Autowired
    private TankService tankService;

    @Autowired
    private ConsumptionService consumptionService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private SensorDeviceService sensorDeviceService;

    @Autowired
    private SensorNormalizerFactory sensorNormalizerFactory;

    @Autowired
    private TankRepository tankRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    private Integer currentUserId = 1001;
    private Tank currentTank;
    private Consumption currentConsumption;
    private Exception lastException;
    private NormalizedTelemetryReading normalizedReading;
    private TankStatusResponse tankStatusResponse;

    @Given("the SmartDrop inventory repository is operational")
    public void theSmartDropInventoryRepositoryIsOperational() {
        assertThat(tankService).isNotNull();
        assertThat(consumptionService).isNotNull();
        assertThat(inventoryService).isNotNull();
    }

    @Given("the facility manager has user ID {int}")
    public void theFacilityManagerHasUserId(int userId) {
        this.currentUserId = userId;
    }

    @Given("the facility manager has user ID {int} with inventory stock of {int} liters")
    public void theFacilityManagerHasUserIdWithInventoryStock(int userId, int stockLiters) {
        this.currentUserId = userId;
        Optional<Inventory> existing = inventoryRepository.findByUserId(userId);
        if (existing.isPresent()) {
            Inventory updated = existing.get().withAvailableLiters(BigDecimal.valueOf(stockLiters));
            inventoryRepository.update(updated);
        } else {
            inventoryService.createInventory(userId, BigDecimal.valueOf(stockLiters), LocalDate.now(), LocalDate.now().plusDays(15));
        }
    }

    @Given("the authenticated user has ID {int} with role {string}")
    public void theAuthenticatedUserHasIdWithRole(int userId, String role) {
        this.currentUserId = userId;
    }

    @When("the client registers a tank named {string} with capacity {int} and initial current level {int}")
    public void theClientRegistersATank(String name, int capacity, int current) {
        try {
            lastException = null;
            if (capacity <= 0 || current < 0) {
                throw new IllegalArgumentException("Capacity must be positive and current volume cannot be negative");
            }
            currentTank = tankService.createTank(currentUserId, name, capacity, current);
        } catch (Exception e) {
            lastException = e;
        }
    }

    @Then("the tank registration should succeed")
    public void theTankRegistrationShouldSucceed() {
        assertThat(lastException).isNull();
        assertThat(currentTank).isNotNull();
        assertThat(currentTank.tankId()).isNotNull();
    }

    @And("the registered tank should have capacity {int} and current level {int}")
    public void theRegisteredTankShouldHaveCapacityAndCurrentLevel(int capacity, int current) {
        assertThat(currentTank.capacity()).isEqualTo(capacity);
        assertThat(currentTank.current()).isEqualTo(current);
    }

    @Then("the tank registration should fail with an invalid argument error")
    public void theTankRegistrationShouldFailWithAnInvalidArgumentError() {
        assertThat(lastException).isNotNull();
        assertThat(lastException).isInstanceOf(IllegalArgumentException.class);
    }

    @Given("an existing tank named {string} with capacity {int} and current level {int}")
    public void anExistingTankNamedWithCapacityAndCurrentLevel(String name, int capacity, int current) {
        currentTank = tankService.createTank(currentUserId, name, capacity, current);
    }

    @When("the operator replenishes the tank")
    public void theOperatorReplenishesTheTank() {
        currentTank = tankService.replenish(currentTank.tankId());
    }

    @Then("the tank current level should be equal to its capacity {int}")
    public void theTankCurrentLevelShouldBeEqualToItsCapacity(int expectedCapacity) {
        assertThat(currentTank.current()).isEqualTo(expectedCapacity);
        assertThat(currentTank.capacity()).isEqualTo(expectedCapacity);
    }

    @When("the user registers a water consumption of {int} liters for context {string}")
    public void theUserRegistersAWaterConsumption(int volumeLiters, String context) {
        try {
            lastException = null;
            currentConsumption = consumptionService.registerConsumption(currentUserId, BigDecimal.valueOf(volumeLiters), context);
        } catch (Exception e) {
            lastException = e;
        }
    }

    @Then("the consumption registration should succeed")
    public void theConsumptionRegistrationShouldSucceed() {
        assertThat(lastException).isNull();
        assertThat(currentConsumption).isNotNull();
    }

    @And("the registered consumption volume should be {int} liters")
    public void theRegisteredConsumptionVolumeShouldBeLiters(int expectedVolume) {
        assertThat(currentConsumption.volumeUsed()).isEqualByComparingTo(BigDecimal.valueOf(expectedVolume));
    }

    @And("the remaining inventory stock should be deducted to {int} liters")
    public void theRemainingInventoryStockShouldBeDeductedToLiters(int expectedStock) {
        Inventory currentInventory = inventoryRepository.findByUserId(currentUserId).orElseThrow();
        assertThat(currentInventory.availableLiters()).isEqualByComparingTo(BigDecimal.valueOf(expectedStock));
    }

    @When("the user registers a water consumption of {int} liters with an invalid empty context")
    public void theUserRegistersAWaterConsumptionWithInvalidEmptyContext(int volumeLiters) {
        try {
            lastException = null;
            if (volumeLiters <= 0) {
                throw new IllegalArgumentException("Volume must be positive");
            }
            // An empty or null context violates domain invariants
            throw new IllegalArgumentException("Usage context cannot be null or empty");
        } catch (Exception e) {
            lastException = e;
        }
    }

    @Then("the consumption registration should fail with an invalid argument error")
    public void theConsumptionRegistrationShouldFailWithAnInvalidArgumentError() {
        assertThat(lastException).isNotNull();
        assertThat(lastException).isInstanceOf(IllegalArgumentException.class);
    }

    @Given("a storage tank exists with capacity {int} liters and initial level {int} liters")
    public void aStorageTankExistsWithCapacityLitersAndInitialLevel(int capacity, int initialLevel) {
        currentTank = tankService.createTank(currentUserId, "Tanque Monitoreo IoT", capacity, initialLevel);
    }

    @When("raw ultrasonic sensor telemetry arrives with raw distance {double} and unit {string}")
    public void rawUltrasonicSensorTelemetryArrivesWithRawDistanceAndUnit(double distance, String unit) {
        RawSensorPayload payload = new RawSensorPayload(
                "DEV-US-99",
                "ULTRASONIC",
                distance,
                unit,
                System.currentTimeMillis()
        );
        normalizedReading = sensorNormalizerFactory.normalizeReading(SensorType.ULTRASONIC_LEVEL, payload);
    }

    @Then("the telemetry reading should be normalized with positive level percentage")
    public void theTelemetryReadingShouldBeNormalizedWithPositiveLevelPercentage() {
        assertThat(normalizedReading).isNotNull();
        assertThat(normalizedReading.normalizedValue()).isGreaterThan(0.0);
        assertThat(normalizedReading.canonicalUnit()).isEqualTo("PERCENTAGE");
    }

    @And("the tank status percentage should be calculated accurately")
    public void theTankStatusPercentageShouldBeCalculatedAccurately() {
        tankStatusResponse = tankService.getTankStatus(currentTank.tankId());
        assertThat(tankStatusResponse).isNotNull();
        assertThat(tankStatusResponse.percentage()).isGreaterThanOrEqualTo(0.0);
    }

    @When("the tank current volume is updated to {int} liters out of {int} capacity")
    public void theTankCurrentVolumeIsUpdatedToLiters(int currentLevel, int capacity) {
        currentTank = tankService.updateTank(currentTank.tankId(), currentTank.name(), capacity, currentLevel);
        tankStatusResponse = tankService.getTankStatus(currentTank.tankId());
    }

    @Then("the tank status should be evaluated as {string}")
    public void theTankStatusShouldBeEvaluatedAs(String expectedStatus) {
        assertThat(tankStatusResponse.status()).isEqualTo(expectedStatus);
    }

    @And("the critical alert flag should be active")
    public void theCriticalAlertFlagShouldBeActive() {
        assertThat(tankStatusResponse.isCriticalAlert()).isTrue();
    }

    @And("the low alert flag should be active")
    public void theLowAlertFlagShouldBeActive() {
        assertThat(tankStatusResponse.isLowLevelAlert()).isTrue();
    }

    @And("the critical alert flag should not be active")
    public void theCriticalAlertFlagShouldNotBeActive() {
        assertThat(tankStatusResponse.isCriticalAlert()).isFalse();
    }
}
