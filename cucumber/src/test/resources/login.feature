@login
Feature: Login

  Scenario: There is a login button
    When we direct the browser to "http://localhost:3000/#/"
    Then we will see an element with an id of "open_login"


  Scenario: Clicking the login button opens the login dialog
    Given we have directed the browser to "http://localhost:3000/#/"
    When we click the element with id "open_login"
    Then we will see an element with an id of "username"
    And we will see an element with an id of "password"
    And we will see an element with an id of "submit_login"


  Scenario: We can login
    Given the following users exist:
      | username         | password  |
      | test@example.com | password3 |
    And we have directed the browser to "http://localhost:3000/#/login/"
    When we enter the value "test@example.com" into the element with id "username"
    And we enter the value "password3" into the element with id "password"
    And we click the element with id "submit_login"
    Then we will be logged in as "test@example.com"
