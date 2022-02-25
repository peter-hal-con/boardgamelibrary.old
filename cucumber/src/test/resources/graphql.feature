@graphql
Feature: GraphQL

  Scenario: Perform a GraphQL query
    Given the following users exist:
      | username          | password  | authorities |
      | admin@example.com | password1 | ROLE_ADMIN  |
    And we are authenticated as "admin@example.com"
    When we perform a GET request on "http://localhost:8080/graphql?query=query%20%7B%20__schema%20%7B%20types%20%7B%20name%20%7D%20%7D%7D"
    Then we get a 200 response
