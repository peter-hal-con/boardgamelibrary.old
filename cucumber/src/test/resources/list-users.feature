@list-users
Feature: List users

  Background:
    Given the following users exist:
      | username              | password  | authorities    |
      | admin@example.com     | password1 | ROLE_ADMIN     |
      | committee@example.com | password2 | ROLE_COMMITTEE |


  Scenario Outline: Only certain users can see the "List Users" button
    Given we are logged in as "<username>"
    When we click the element with id "user_dropdown"
    Then we <will_or_will_not> see an element with id "list_users"

    Examples:
      | username              | will_or_will_not |
      | admin@example.com     | will             |
      | committee@example.com | will not         |


  Scenario: The user list screen contains a user_list
    Given we are logged in as "admin@example.com"
    When we click the element with id "user_dropdown"
    And we click the element with id "list_users"
    Then we will see an element with id "user_list"


  Scenario: The user list screen contains list of users
    Given the following users exist:
      | username              | password  |
      | test@example.com      | password3 |
    And we are logged in as "admin@example.com"
    When we click the element with id "user_dropdown"
    And we click the element with id "list_users"
    Then we will see an element with id "tr-admin@example.com"
    And we will see an element with id "tr-committee@example.com"
    And we will see an element with id "tr-test@example.com"
