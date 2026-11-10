@inventory @tanks
Feature: US01 - Residential and Brewery Tank Registration and Capacity Management
  As a facility or home manager
  I want to register storage tanks and cisterns in the SmartDrop inventory
  So that the platform can monitor liquid volume, capacity, and current balance in real time.

  Background:
    Given the SmartDrop inventory repository is operational
    And the facility manager has user ID 1001

  Scenario: Successful registration of a water storage tank
    When the client registers a tank named "Tanque Principal 1" with capacity 5000 and initial current level 4200
    Then the tank registration should succeed
    And the registered tank should have capacity 5000 and current level 4200

  Scenario: Rejection of tank registration with negative or invalid capacity
    When the client registers a tank named "Tanque Invalido" with capacity -500 and initial current level 0
    Then the tank registration should fail with an invalid argument error

  Scenario: Full tank replenishment to maximum capacity
    Given an existing tank named "Tanque Fermentacion" with capacity 3000 and current level 500
    When the operator replenishes the tank
    Then the tank current level should be equal to its capacity 3000
