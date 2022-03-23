@authorization
Feature: Authorization

  Background:
    Given the following users exist:
      | username              | password  | authorities    |
      | admin@example.com     | password1 | ROLE_ADMIN     |
      | committee@example.com | password2 | ROLE_COMMITTEE |


  Scenario Outline: Retrieving a list of users
    Given we are authenticated as "<username>"
    And we perform a GraphQL query "query{userList{username}}"
    Then we get a <response> response

    Examples:
      | username              | password  | response |
      | admin@example.com     | password1 | 200      |
      | committee@example.com | password2 | 403      |
