@user-crud
Feature: Creation, retrieval, update, and deletion of users
  Background: We are logged in as an admin
    Given the following users exist:
      | username              | password  | authorities    |
      | admin@example.com     | password1 | ROLE_ADMIN     |
    And we are authenticated as "admin@example.com"


  @create
  Scenario: We can create a user
    When we create a user with username "test@example.com" and password "password3"
    Then the user with username "test@example.com" will have the password "password3"


  @create
  Scenario: We can create a user with an authority
    When we create a user with username "test@example.com" and password "password3" and authority "<authority>"
    And we retrieve the user with username "test@example.com"
    Then the result of "$.data.user.authorities[0].authority" will be "<authority>"

    Examples:
      | authority      |
      | ROLE_ADMIN     |
      | ROLE_COMMITTEE |


  @retrieve
  Scenario: We can retrieve a list of users
    Given the following users exist:
      | username              | password  | authorities    |
      | committee@example.com | password2 | ROLE_COMMITTEE |
    When we perform a GraphQL query "query{userList{username authorities {authority}}}"
    Then the result of "$.data.userList[?(@.username=='admin@example.com')]" will be '[{"username":"admin@example.com","authorities":[{"authority":"ROLE_ADMIN"}]}]'
    And  the result of "$.data.userList[?(@.username=='committee@example.com')]" will be '[{"username":"committee@example.com","authorities":[{"authority":"ROLE_COMMITTEE"}]}]'


  @retrieve
  Scenario Outline: We can retrieve an individual user
    Given the following users exist:
      | username              | password  | authorities    |
      | committee@example.com | password2 | ROLE_COMMITTEE |
    When we retrieve the user with username "<username>"
    Then the result of "$.data.user.authorities[0].authority" will be "<result>"

    Examples:
      | username              | result         |
      | admin@example.com     | ROLE_ADMIN     |
      | committee@example.com | ROLE_COMMITTEE |


  @retrieve
  Scenario: We can retrieve an individual user by username
    When we perform a GraphQL query 'query{userByUsername(username:"admin@example.com"){id}}'
    Then the result of "$.data.userByUsername.id" will have a value


  @update
  Scenario: We can change a user's username
    Given the following users exist:
      | username         | password  |
      | test@example.com | password3 |
    When we update the user with username "test@example.com" so that their "username" is "updated@example.com"
    And we retrieve the user with username "test@example.com"
    Then the result of "$.data.user.username" will be "updated@example.com"


  @update
  Scenario: We can change a user's password
    Given the following users exist:
      | username         | password  |
      | test@example.com | password3 |
    When we update the user with username "test@example.com" so that their "password" is "updatedPassword"
    Then the user with username "test@example.com" will have the password "updatedPassword"


  @update
  Scenario Outline: We can grant an authority to a user
    Given the following users exist:
      | username         | password  |
      | test@example.com | password3 |
    When we grant the "<authority>" authority to the user with username "test@example.com"
    And we retrieve the user with username "test@example.com"
    Then the result of "$.data.user.authorities[0].authority" will be "<authority>"

    Examples:
      | authority      |
      | ROLE_ADMIN     |
      | ROLE_COMMITTEE |


  @update
  Scenario Outline: We can revoke an authority from a user
    Given the following users exist:
      | username         | password  | authorities |
      | test@example.com | password3 | <authority> |
    When we revoke the "<authority>" authority from the user with username "test@example.com"
    And we retrieve the user with username "test@example.com"
    Then the result of "$.data.user.authorities" will be "[]"

    Examples:
      | authority      |
      | ROLE_ADMIN     |
      | ROLE_COMMITTEE |


  @delete
  Scenario: We can delete a user
    Given the following users exist:
      | username         | password  |
      | test@example.com | password3 |
    When we delete the user with username "test@example.com"
    And we retrieve the user with username "test@example.com"
    Then the result of "$.data.user" will be 'null'


  @delete
  Scenario Outline: We can delete a user with an authority
    Given the following users exist:
      | username         | password  | authorities |
      | test@example.com | password3 | <authority> |
    When we delete the user with username "test@example.com"
    And we retrieve the user with username "test@example.com"
    Then the result of "$.data.user" will be 'null'

    Examples:
      | authority      |
      | ROLE_ADMIN     |
      | ROLE_COMMITTEE |