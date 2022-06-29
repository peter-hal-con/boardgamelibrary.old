@create-copy
Feature: Create library items

  Background:
    Given the following users exist:
      | username              | password  | authorities    |
      | admin@example.com     | password1 | ROLE_ADMIN     |
      | committee@example.com | password2 | ROLE_COMMITTEE |
      | test@example.com      | password3 |                |


  Scenario Outline: Only certain users can see the "Create Library Items" button
    Given we are logged in as "<username>"
    When we click the element with id "user_dropdown"
    Then we <will_or_will_not> see an element with id "create_copy"

    Examples:
      | username              | will_or_will_not |
      | admin@example.com     | will             |
      | committee@example.com | will             |
      | test@example.com      | will not         |


  Scenario: The create library item dialog
    Given we are logged in as "admin@example.com"
    When we click the element with id "user_dropdown"
    And we click the element with id "create_copy"
    Then we will see an element with id "copy_owner"
    And we will see an element with id "copy_game"
    And we will see an element with id "submit_create_copy"


  Scenario: The create library item dialog will list all users when an admin user is logged in
    Given we are logged in as "admin@example.com"
    When we click the element with id "user_dropdown"
    And we click the element with id "create_copy"
    Then the select with id "copy_owner" will have an option "admin@example.com"
    Then the select with id "copy_owner" will have an option "committee@example.com"
    Then the select with id "copy_owner" will have an option "test@example.com"


  Scenario: The create library item dialog will list only the logged in users when an committee user is logged in
    Given we are logged in as "committee@example.com"
    When we click the element with id "user_dropdown"
    And we click the element with id "create_copy"
    Then the select with id "copy_owner" will have an option "committee@example.com"
    Then the select with id "copy_owner" will not have an option "admin@example.com"
    Then the select with id "copy_owner" will not have an option "test@example.com"
