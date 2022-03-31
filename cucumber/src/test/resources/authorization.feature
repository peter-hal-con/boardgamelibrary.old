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


  Scenario Outline: Creating a new user
    Given we are authenticated as "<username>"
    When we perform a GraphQL query "mutation{userCreate(user:{username:\"test@example.com\" password:\"password3\" accountLocked:false accountExpired:false passwordExpired:false enabled:true}){id}}"
    Then the result of "$.data.userCreate" <will_or_will_not> be "null"

    Examples:
      | username              | will_or_will_not |
      | admin@example.com     | will not         |
      | committee@example.com | will             |
