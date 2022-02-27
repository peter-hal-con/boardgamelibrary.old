@selenium
Feature: Selenium

  Scenario: Connecting a browser to the client
    When we direct the browser to "http://localhost:8080"
    Then we will see an element with id "app"
