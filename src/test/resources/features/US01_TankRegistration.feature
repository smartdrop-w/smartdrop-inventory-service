@inventory @tanks
Feature: US01 - Residential and Brewery Tank Registration
  As a facility or home manager
  I want to register storage tanks and cisterns in the SmartDrop inventory
  So that the platform can monitor liquid volume, capacity, and current balance in real time.

  Background:
    Given the authenticated user has ID 1001 with role "ROLE_USER"

  Scenario: Successful registration of a water storage tank
    When the client sends a POST request to "/api/v1/inventory/tanks" with payload:
      | name               | capacity | current | liquidType | userId |
      | Tanque Principal 1 | 5000     | 4200    | WATER      | 1001   |
    Then the response status code should be 201 Created
    And the response body should contain the generated "tankId"
    And the tank "capacity" should be 5000 and "current" balance should be 4200

  Scenario: Rejection of tank registration with negative capacity
    When the client sends a POST request to "/api/v1/inventory/tanks" with payload:
      | name           | capacity | current | liquidType | userId |
      | Tanque Invalido| -500     | 0       | WATER      | 1001   |
    Then the response status code should be 400 Bad Request
