@update-user
Feature: Update users

  Background:
    Given the following users exist:
      | username              | password  | authorities    |
      | admin@example.com     | password1 | ROLE_ADMIN     |
      | committee@example.com | password2 | ROLE_COMMITTEE |


  Scenario: The user list includes an update button
    Given we are logged in as "admin@example.com"
    When we click the element with id "user_dropdown"
    And we click the element with id "list_users"
	Then we will see an element with xpath "//table[@id='user_list']//tr[@id='tr-committee@example.com']//td[@id='update']//a"


  Scenario: The update user dialog
    Given we are logged in as "admin@example.com"
    When we click the element with id "user_dropdown"
    And we click the element with id "list_users"
	And we click the element with xpath "//table[@id='user_list']//tr[@id='tr-committee@example.com']//td[@id='update']//a"
    Then we will see an element with id "username"
    And the element with id "username" will contain "committee@example.com"
    And we will see an element with id "password"
    And the element with id "password" will be empty
    And we will see an element with id "confirm_password"
    And the element with id "confirm_password" will be empty
    And we will see an element with id "authority_ROLE_ADMIN"
    And the element with id "authority_ROLE_ADMIN" will be unchecked
    And we will see an element with id "authority_ROLE_COMMITTEE"
    And the element with id "authority_ROLE_COMMITTEE" will be checked
    And we will see an element with id "submit_update_user"


  Scenario: Submitting a user update returns us to the user list screen
    Given the following users exist:
      | username              | password  |
      | test@example.com      | password1 |
    And we are logged in as "admin@example.com"
    When we click the element with id "user_dropdown"
    And we click the element with id "list_users"
	And we click the element with xpath "//table[@id='user_list']//tr[@id='tr-test@example.com']//td[@id='update']//a"
    And we enter the value "test2@example.com" into the element with id "username"
    And we click the element with id "submit_update_user"
    Then we will see an element with id "user_list"


  Scenario: Successfully changing a user's username
    Given the following users exist:
      | username              | password  |
      | test@example.com      | password1 |
    And we are logged in as "admin@example.com"
    When we click the element with id "user_dropdown"
    And we click the element with id "list_users"
	And we click the element with xpath "//table[@id='user_list']//tr[@id='tr-test@example.com']//td[@id='update']//a"
    And we enter the value "test2@example.com" into the element with id "username"
    And we click the element with id "submit_update_user"
    Then the user with username "test2@example.com" will have the password "password1"


  Scenario: Successfully changing a user's password
    Given the following users exist:
      | username              | password  |
      | test@example.com      | password1 |
    And we are logged in as "admin@example.com"
    When we click the element with id "user_dropdown"
    And we click the element with id "list_users"
	And we click the element with xpath "//table[@id='user_list']//tr[@id='tr-test@example.com']//td[@id='update']//a"
    And we enter the value "password2" into the element with id "password"
    And we enter the value "password2" into the element with id "confirm_password"
    And we click the element with id "submit_update_user"
	Then the user with username "test@example.com" will have the password "password2"


  Scenario: Successfully adding a role to a user
    Given the following users exist:
      | username              | password  |
      | test@example.com      | password1 |
    And we are logged in as "admin@example.com"
    When we click the element with id "user_dropdown"
    And we click the element with id "list_users"
	And we click the element with xpath "//table[@id='user_list']//tr[@id='tr-test@example.com']//td[@id='update']//a"
    And we click the element with id "authority_ROLE_COMMITTEE"
    And we click the element with id "submit_update_user"
    Then the user with username "test@example.com" will have the authority "ROLE_COMMITTEE"
    And the user with username "test@example.com" will not have the authority "ROLE_ADMIN"


  Scenario: Successfully removing a role from a user
    Given the following users exist:
      | username              | password  | authorities    |
      | test@example.com      | password1 | ROLE_COMMITTEE |
    And we are logged in as "admin@example.com"
    When we click the element with id "user_dropdown"
    And we click the element with id "list_users"
	And we click the element with xpath "//table[@id='user_list']//tr[@id='tr-test@example.com']//td[@id='update']//a"
    And we click the element with id "authority_ROLE_COMMITTEE"
    And we click the element with id "submit_update_user"
    Then the user with username "test@example.com" will not have the authority "ROLE_COMMITTEE"
    And the user with username "test@example.com" will not have the authority "ROLE_ADMIN"


  Scenario: When the password and confirm_password field values are different an alert will appear and the submit button will be disabled
    Given the following users exist:
      | username              | password  |
      | test@example.com      | password1 |
    And we are logged in as "admin@example.com"
    When we click the element with id "user_dropdown"
    And we click the element with id "list_users"
	And we click the element with xpath "//table[@id='user_list']//tr[@id='tr-test@example.com']//td[@id='update']//a"
    And we enter the value "password3" into the element with id "password"
    And we enter the value "passwordThree" into the element with id "confirm_password"
    Then we will see an element with id "password_mismatch"
    And the element with id "submit_update_user" will be disabled


  Scenario: When the password and confirm_password field values are too short an alert will appear and the submit button will be disabled
    Given the following users exist:
      | username              | password  |
      | test@example.com      | password1 |
    And we are logged in as "admin@example.com"
    When we click the element with id "user_dropdown"
    And we click the element with id "list_users"
	And we click the element with xpath "//table[@id='user_list']//tr[@id='tr-test@example.com']//td[@id='update']//a"
    When we enter the value "pass" into the element with id "password"
    And we enter the value "pass" into the element with id "confirm_password"
    Then we will see an element with id "password_too_short"
    And the element with id "submit_update_user" will be disabled


  Scenario: When the username field value is not an email address an alert will appear and the submit button will be disabled
    Given the following users exist:
      | username              | password  |
      | test@example.com      | password1 |
    And we are logged in as "admin@example.com"
    When we click the element with id "user_dropdown"
    And we click the element with id "list_users"
	And we click the element with xpath "//table[@id='user_list']//tr[@id='tr-test@example.com']//td[@id='update']//a"
    And we enter the value "test_at_example.com" into the element with id "username"
    Then we will see an element with id "username_not_email"
    And the element with id "submit_update_user" will be disabled
