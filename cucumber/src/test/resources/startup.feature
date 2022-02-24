@startup
Feature: Starting up the application

  Scenario: The server is started
    When we perform a GET request on "http://localhost:8080/application"
    Then we get a 200 response

  Scenario: The client is started
    When we perform a GET request on "http://localhost:3000/#/"
    Then we get a 200 response
