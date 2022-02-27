@login
Feature: Login

  Scenario: There is a login button
    When we direct the browser to "http://localhost:8080/#/"
    Then we will see an element with id "open_login"


  Scenario: Clicking the login button opens the login dialog
    Given we have directed the browser to "http://localhost:8080/#/"
    When we click the element with id "open_login"
    Then we will see an element with id "username"
    And we will see an element with id "password"
    And we will see an element with id "submit_login"


  Scenario: We can login
    Given the following users exist:
      | username         | password  |
      | test@example.com | password3 |
    And we have directed the browser to "http://localhost:8080/#/login/"
    When we enter the value "test@example.com" into the element with id "username"
    And we enter the value "password3" into the element with id "password"
    And we click the element with id "submit_login"
    Then we will be logged in as "test@example.com"
    And the current URL will be "http://localhost:8080/#/"
    And we will see an element with id "user_dropdown"
    And we will not see an element with id "open_login"


  Scenario: Failed login should display alert
    Given the following users exist:
      | username         | password  |
      | test@example.com | password3 |
    And we have directed the browser to "http://localhost:8080/#/login/"
    When we enter the value "test@example.com" into the element with id "username"
    And we enter the value "wrong_password" into the element with id "password"
    And we click the element with id "submit_login"
    Then we will not be logged in
    And we will see an element with id "login_failed"


  Scenario: There is a logout option in the user dropdown
    Given the following users exist:
      | username         | password  |
      | test@example.com | password3 |
    And we are logged in as "test@example.com"
    When we click the element with id "user_dropdown"
    Then we will see an element with id "logout"


  Scenario: We can logout
    Given the following users exist:
      | username         | password  |
      | test@example.com | password3 |
    And we are logged in as "test@example.com"
    And we have directed the browser to "http://localhost:8080/#/someplace/"
    When we click the element with id "user_dropdown"
    And we click the element with id "logout"
    Then we will not be logged in
    And the current URL will be "http://localhost:8080/#/"
    And we will not see an element with id "user_dropdown"
    And we will see an element with id "open_login"


  Scenario: Logging out means not having access to restricted endpoints
    Given the following users exist:
      | username          | password  | authorities |
      | admin@example.com | password3 | ROLE_ADMIN  |
    And we are logged in as "admin@example.com"
    When we click the element with id "user_dropdown"
    And we click the element with id "logout"
    And we direct the browser to "http://localhost:8080/application"
    Then we will see an element with id "login"
