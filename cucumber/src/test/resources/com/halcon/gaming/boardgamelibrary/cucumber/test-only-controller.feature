@test-only-controller
Feature: Test Only Controller

  Scenario: The test only controller is available
    When we perform a GET request on "http://localhost:8080/testOnly"
    Then we get a 200 response
