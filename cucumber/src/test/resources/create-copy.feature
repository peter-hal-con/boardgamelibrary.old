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
    And we will see an element with id "copy_title"
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


  Scenario: The create library item dialog will list only the logged in user when an committee user is logged in
    Given the following users exist:
      | username              | password  | authorities    |
      | admin2@example.com    | password4 | ROLE_ADMIN     |
    And we are logged in as "admin2@example.com"
    When we click the element with id "user_dropdown"
    And we click the element with id "create_copy"
    Then the select with id "copy_owner" will have the "admin2@example.com" option selected


  Scenario: The create library item dialog will display a message when a new copy is created by an admin user
    Given we are logged in as "admin@example.com"
    When we click the element with id "user_dropdown"
    And we click the element with id "create_copy"
    And we enter the value "Crossbows and Catapults" into the bgg autocomplete with id "copy_title"
    And we click the element with id "submit_create_copy"
    Then we will see an element with id "message_success"


  Scenario: The create library item dialog will display a message when a new copy is created by a committee user
    Given we are logged in as "committee@example.com"
    When we click the element with id "user_dropdown"
    And we click the element with id "create_copy"
    And we enter the value "Crossbows and Catapults" into the bgg autocomplete with id "copy_title"
    And we click the element with id "submit_create_copy"
    Then we will see an element with id "message_success"


  Scenario: The create library item dialog will create a new title when the selected title does not already exist
    Given we are logged in as "admin@example.com"
    When we click the element with id "user_dropdown"
    And we click the element with id "create_copy"
    And we enter the value "Crossbows and Catapults" into the bgg autocomplete with id "copy_title"
    And we click the element with id "submit_create_copy"
    Then we will see an element with id "message_success"
    And there will be a title with the name "Crossbows and Catapults"


  Scenario: The create library item dialog will not create a new title when the selected title already exists
    Given we have created a title with name "Crossbows and Catapults" and bggid 2129
    And we are logged in as "admin@example.com"
    When we click the element with id "user_dropdown"
    And we click the element with id "create_copy"
    And we enter the value "Crossbows and Catapults" into the bgg autocomplete with id "copy_title"
    And we click the element with id "submit_create_copy"
    Then we will see an element with id "message_success"
    And there will be a title with the name "Crossbows and Catapults"


  Scenario: The create library item dialog will create a new copy
    Given we are logged in as "admin@example.com"
    When we click the element with id "user_dropdown"
    And we click the element with id "create_copy"
    And we enter the value "Crossbows and Catapults" into the bgg autocomplete with id "copy_title"
    And we click the element with id "submit_create_copy"
    Then we will see an element with id "message_success"
    And there will be a copy of the title named "Crossbows and Catapults" belonging to "admin@example.com"


  Scenario: The create library item dialog will clear the copy_title field after creating a copy
    Given we are logged in as "admin@example.com"
    When we click the element with id "user_dropdown"
    And we click the element with id "create_copy"
    And we enter the value "Crossbows and Catapults" into the bgg autocomplete with id "copy_title"
    And we click the element with id "submit_create_copy"
    Then the element with id "copy_title" will be empty


  Scenario Outline: The create library item dialog will create a new copy of a game whose title includes an ampersand
    Given we are logged in as "admin@example.com"
    When we click the element with id "user_dropdown"
    And we click the element with id "create_copy"
    And we enter the value "<title>" into the bgg autocomplete with id "copy_title"
    And we click the element with id "submit_create_copy"
    Then we will see an element with id "message_success"
    And there will be a copy of the title named "<title>" belonging to "admin@example.com"

    Examples:
      | title              |
      | Tigris & Euphrates |
      | Light & Dark       |
      | Hare & Tortoise    |
