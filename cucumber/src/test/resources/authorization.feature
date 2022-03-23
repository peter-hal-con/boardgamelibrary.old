@authorization
Feature: Authorization

  Background:
    Given the following users exist:
      | username              | password  | authorities    |
      | admin@example.com     | password1 | ROLE_ADMIN     |
      | committee@example.com | password2 | ROLE_COMMITTEE |


  Scenario Outline: Retrieving a list of users
    Given we are authenticated as "<username>"
    When we perform a GraphQL query "query{userList{username}}"
    Then the result of "$.data.userList.length()" will be "<count>"

    Examples:
      | username              | count |
      | admin@example.com     | 2     |
      | committee@example.com | null  |
