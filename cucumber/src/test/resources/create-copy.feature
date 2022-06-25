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
    Then we will see an element with id "copy_title"
    And we will see an element with id "submit_create_copy"

