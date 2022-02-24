@selenium
Feature: Selenium

  Scenario: Connecting a browser to the client
    When we direct the browser to "http://localhost:3000"
    Then we will see an element with an id of "app"
