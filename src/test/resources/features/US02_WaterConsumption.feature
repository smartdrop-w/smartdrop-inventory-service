@inventory @consumption
Feature: US02 - Water Consumption Registration and Balance Deduction
  As an operator or homeowner
  I want to register an episodic water consumption
  So that the tank's current volume is automatically deducted and persisted atomically.

  Background:
    Given a tank exists with ID 501 having a capacity of 5000 liters and current balance of 3000 liters

  Scenario: Successful consumption deduction
    When the client sends a POST request to "/api/v1/inventory/consumptions" with payload:
      | userId | volumeUsed | usageContext                 |
      | 1001   | 250        | Lavado de fermentador conico |
    Then the response status code should be 201 Created
    And the transaction should be committed to the inventory database
    And the current tank balance should be reduced by 250 liters

  Scenario: Rejection of consumption exceeding maximum characters in usage context
    When the client sends a POST request to "/api/v1/inventory/consumptions" with a context exceeding 50 characters
    Then the response status code should be 400 Bad Request
    And the error details should indicate "Usage context exceeds 50 characters limit"
