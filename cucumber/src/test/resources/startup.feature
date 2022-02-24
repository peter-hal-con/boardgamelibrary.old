@startup
Feature: Starting up the application

  Scenario: The server is started
    Given the following users exist:
      | username          | password  | authorities |
      | admin@example.com | password1 | ROLE_ADMIN  |
    And we are authenticated as "admin@example.com"
    When we perform a GET request on "http://localhost:8080/application"
    Then we get a 200 response

  Scenario: The client is started
    When we perform a GET request on "http://localhost:3000/#/"
    Then we get a 200 response
