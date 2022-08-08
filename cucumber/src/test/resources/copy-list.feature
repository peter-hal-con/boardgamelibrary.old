@copy-list
Feature: copyList graphQL query

  Background:
    Given the following users exist:
      | username              | password  | authorities    |
      | admin@example.com     | password1 | ROLE_ADMIN     |
      | committee@example.com | password2 | ROLE_COMMITTEE |
    And we are authenticated as "admin@example.com"
    And we have created a title with name "Some Title"


  Scenario: An admin user can see all copies
    Given we are authenticated as "admin@example.com"
    And we have created a copy of "Some Title" belonging to "committee@example.com"
    When we perform a GraphQL query "query{copyList{owner{username} title{name}}}"
    Then the result of "$.data.copyList[0].owner.username" will be "committee@example.com"
    And  the result of "$.data.copyList[0].title.name" will be "Some Title"


  Scenario: A committee user can see their own copies
    Given we are authenticated as "committee@example.com"
    And we have created a copy of "Some Title" belonging to "committee@example.com"
    When we perform a GraphQL query "query{copyList(owner:\"committee@example.com\"){owner{username} title{name}}}"
    Then the result of "$.data.copyList[0].owner.username" will be "committee@example.com"
    And  the result of "$.data.copyList[0].title.name" will be "Some Title"

