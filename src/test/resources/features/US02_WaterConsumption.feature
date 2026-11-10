@inventory @consumption
Feature: US02 - Water Consumption Registration and Inventory Deduction
  As an operator or brewery manager
  I want to register episodic water consumption transactions
  So that the consumption history is preserved and the inventory balance is consistently updated.

  Background:
    Given the SmartDrop inventory repository is operational
    And the facility manager has user ID 1001 with inventory stock of 8000 liters

  Scenario: Successful consumption deduction
    When the user registers a water consumption of 250 liters for context "Lavado de fermentador conico"
    Then the consumption registration should succeed
    And the registered consumption volume should be 250 liters
    And the remaining inventory stock should be deducted to 7750 liters

  Scenario: Rejection of consumption with invalid or empty context
    When the user registers a water consumption of 100 liters with an invalid empty context
    Then the consumption registration should fail with an invalid argument error
