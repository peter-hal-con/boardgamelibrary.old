@title-crud
Feature: Creation, retrieval, update, and deletion of titles
  Background: We are logged in as an admin
    Given the following users exist:
      | username              | password  | authorities    |
      | admin@example.com     | password1 | ROLE_ADMIN     |
    And we are authenticated as "admin@example.com"


  @create
  Scenario: We can create a title
    When we perform a GraphQL query 'mutation{titleCreate(title:{name:"Some Title"}) {id, name}}'
    Then the result of "$.data.titleCreate.name" will be 'Some Title'


  @create
  Scenario: We can create a title with a bggId
    When we perform a GraphQL query 'mutation{titleCreate(title:{name:"Crossbows and Catapults", bggId:2129}) {id, name, bggId}}'
    Then the result of "$.data.titleCreate.name" will be 'Crossbows and Catapults'
    And the result of "$.data.titleCreate.bggId" will be '2129'


  @create
  Scenario: We cannot create multiple titles with the same bggId
    Given we have created a title with name "Crossbows and Catapults" and bggid 2129
    When we perform a GraphQL query 'mutation{titleCreate(title:{name:"Crossbows and Catapults", bggId:2129}) {id}}'
    Then the result of "$.data.titleCreate.id" will be 'null'


  @retrieve
  Scenario: We can retrieve a title
    Given we have performed a GraphQL query 'mutation{titleCreate(title:{name:"Some Title"}) {id}}'
    When we perform a GraphQL query "query{titleList{name}}"
    Then the result of "$.data.titleList[0]" will be '[name:Some Title]'


  @retrieve
  Scenario: We can retrieve a list of titles
    Given we have performed a GraphQL query 'mutation{titleCreate(title:{name:"Crossbows and Catapults", bggId:2129}) {id}}'
    When we perform a GraphQL query "query{titleList{name, bggId}}"
    Then the result of "$.data.titleList[0]" will be '[name:Crossbows and Catapults, bggId:2129]'


  @retrieve
  Scenario: We can retrieve a title by its bggId
    Given we have performed a GraphQL query 'mutation{titleCreate(title:{name:"Crossbows and Catapults", bggId:2129}) {id}}'
    When we perform a GraphQL query "query{titleByBggId(bggId:2129){name, bggId}}"
    Then the result of "$.data.titleByBggId" will be '[name:Crossbows and Catapults, bggId:2129]'


  @retrieve
  Scenario: We can retrieve a title by its title
    Given we have performed a GraphQL query 'mutation{titleCreate(title:{name:"Crossbows and Catapults", bggId:2129}) {id}}'
    When we perform a GraphQL query 'query{titleByName(name:"Crossbows and Catapults"){name, bggId}}'
    Then the result of "$.data.titleByName" will be '[name:Crossbows and Catapults, bggId:2129]'
