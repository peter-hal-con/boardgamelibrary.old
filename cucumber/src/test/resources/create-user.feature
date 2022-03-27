@create-user
Feature: Create users

  Background:
    Given the following users exist:
      | username              | password  | authorities    |
      | admin@example.com     | password1 | ROLE_ADMIN     |
      | committee@example.com | password2 | ROLE_COMMITTEE |


  Scenario Outline: Only certain users can see the "Create User" button
    Given we are logged in as "<username>"
    When we click the element with id "user_dropdown"
    Then we <will_or_will_not> see an element with id "create_user"

    Examples:
      | username              | will_or_will_not |
      | admin@example.com     | will             |
      | committee@example.com | will not         |


  Scenario: The create user dialog
    Given we are logged in as "admin@example.com"
    When we click the element with id "user_dropdown"
    And we click the element with id "create_user"
    Then we will see an element with id "username"
    And we will see an element with id "password"
    And we will see an element with id "confirm_password"
    And we will see an element with id "submit_create_user"


  Scenario: Creating a user
    Given we are logged in as "admin@example.com"
    When we click the element with id "user_dropdown"
    And we click the element with id "create_user"
    And we enter the value "test@example.com" into the element with id "username"
    And we enter the value "password3" into the element with id "password"
    And we enter the value "password3" into the element with id "confirm_password"
    And we click the element with id "submit_create_user"
    Then the user with username "test@example.com" will have the password "password3"


  Scenario: When password and confirm_password field values are different an alert will appear and the submit button will be disabled
    Given we are logged in as "admin@example.com"
    When we click the element with id "user_dropdown"
    And we click the element with id "create_user"
    And we enter the value "test@example.com" into the element with id "username"
    And we enter the value "password3" into the element with id "password"
    And we enter the value "passwordThree" into the element with id "confirm_password"
    Then we will see an element with id "password_mismatch"
    And the element with id "submit_create_user" will be disabled


  Scenario: When password and confirm_password field values are too short an alert will appear and the submit button will be disabled
    Given we are logged in as "admin@example.com"
    When we click the element with id "user_dropdown"
    And we click the element with id "create_user"
    And we enter the value "test@example.com" into the element with id "username"
    When we enter the value "pass" into the element with id "password"
    And we enter the value "pass" into the element with id "confirm_password"
    Then we will see an element with id "password_too_short"
    And the element with id "submit_create_user" will be disabled
