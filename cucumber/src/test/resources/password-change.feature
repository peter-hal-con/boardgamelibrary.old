@password-change
Feature: User self service password change

  Background:
    Given the following users exist:
      | username         | password  |
      | test@example.com | password3 |
    And we are logged in as "test@example.com"


  Scenario: User dropdown includes password change item
    Given we have directed the browser to "/#/"
    When we click the element with id "user_dropdown"
    Then we will see an element with id "password_change"


  Scenario: Opening the password change form
    Given we have directed the browser to "/#/"
    When we click the element with id "user_dropdown"
    And we click the element with id "password_change"
    Then we will see an element with id "new_password"
    And we will see an element with id "confirm_new_password"
    And we will see an element with id "submit_change_password"
    And we will not see an element with id "password_mismatch"


  Scenario: Performing a password change
    Given we have directed the browser to "/#/"
    When we click the element with id "user_dropdown"
    And we click the element with id "password_change"
    When we enter the value "updatedPassword" into the element with id "new_password"
    And we enter the value "updatedPassword" into the element with id "confirm_new_password"
    And we click the element with id "submit_change_password"
    Then the browser will be directed to "/#/"
    And the user with username "test@example.com" will have the password "updatedPassword"


  Scenario: When new_password and confirm_new_password are different an alert will appear and the submit button will be disabled
    Given we have directed the browser to "/#/change-password"
    When we enter the value "updatedPassword" into the element with id "new_password"
    And we enter the value "differentPassword" into the element with id "confirm_new_password"
    Then we will see an element with id "password_mismatch"
    And the element with id "submit_change_password" will be disabled


  Scenario: When new_password and confirm_new_password are too short an alert will appear and the submit button will be disabled
    Given we have directed the browser to "/#/change-password"
    When we enter the value "shrt" into the element with id "new_password"
    And we enter the value "shrt" into the element with id "confirm_new_password"
    Then we will see an element with id "password_too_short"
    And the element with id "submit_change_password" will be disabled


  Scenario: When new_password is sufficiently long, the password_too_short alert will disappear
    Given we have directed the browser to "/#/change-password"
    When we enter the value "long enough" into the element with id "new_password"
    Then we will not see an element with id "password_too_short"
