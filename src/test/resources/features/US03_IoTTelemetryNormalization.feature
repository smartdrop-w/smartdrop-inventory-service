@inventory @telemetry @iot
Feature: US03 - IoT Sensor Telemetry Normalization and Critical Alert Thresholds
  As an automated IoT ingestion pipeline
  I want to normalize raw telemetry from various sensor manufacturers (ultrasonic, pressure, flow)
  So that tank liquid volumes are updated and critical low-level alerts are triggered when below safety thresholds.

  Background:
    Given the SmartDrop inventory repository is operational
    And a storage tank exists with capacity 5000 liters and initial level 2000 liters

  Scenario: Processing normalized ultrasonic telemetry and updating tank volume
    When raw ultrasonic sensor telemetry arrives with raw distance 50.0 and unit "CM"
    Then the telemetry reading should be normalized with positive level percentage
    And the tank status percentage should be calculated accurately

  Scenario: Critical threshold alert when tank liquid falls below 15 percent
    When the tank current volume is updated to 600 liters out of 5000 capacity
    Then the tank status should be evaluated as "CRITICAL"
    And the critical alert flag should be active

  Scenario: Low threshold alert when tank liquid falls below 30 percent but above 15 percent
    When the tank current volume is updated to 1200 liters out of 5000 capacity
    Then the tank status should be evaluated as "LOW"
    And the low alert flag should be active
    And the critical alert flag should not be active
