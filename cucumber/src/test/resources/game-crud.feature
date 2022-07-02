@game-crud
Feature: Creation, retrieval, update, and deletion of games
  Background: We are logged in as an admin
    Given the following users exist:
      | username              | password  | authorities    |
      | admin@example.com     | password1 | ROLE_ADMIN     |
    And we are authenticated as "admin@example.com"


  @create
  Scenario: We can create a game
    When we perform a GraphQL query 'mutation{gameCreate(game:{title:"Some Title"}) {id, title}}'
    Then the result of "$.data.gameCreate.title" will be 'Some Title'


  @create
  Scenario: We can create a game with a bggId
    When we perform a GraphQL query 'mutation{gameCreate(game:{title:"Crossbows and Catapults", bggId:2129}) {id, title, bggId}}'
    Then the result of "$.data.gameCreate.title" will be 'Crossbows and Catapults'
    And the result of "$.data.gameCreate.bggId" will be '2129'


  @this-one
  @create
  Scenario: We cannot create multiple games with the same bggId
    Given we have performed a GraphQL query 'mutation{gameCreate(game:{title:"Crossbows and Catapults", bggId:2129}) {id}}'
    When we perform a GraphQL query 'mutation{gameCreate(game:{title:"Crossbows and Catapults", bggId:2129}) {id}}'
    Then the result of "$.data.gameCreate.id" will be 'null'


  @retrieve
  Scenario: We can retrieve a game
    Given we have performed a GraphQL query 'mutation{gameCreate(game:{title:"Some Title"}) {id}}'
    When we perform a GraphQL query "query{gameList{title}}"
    Then the result of "$.data.gameList[0]" will be '[title:Some Title]'


  @retrieve
  Scenario: We can retrieve a list of games
    Given we have performed a GraphQL query 'mutation{gameCreate(game:{title:"Crossbows and Catapults", bggId:2129}) {id}}'
    When we perform a GraphQL query "query{gameList{title, bggId}}"
    Then the result of "$.data.gameList[0]" will be '[title:Crossbows and Catapults, bggId:2129]'


  @retrieve
  Scenario: We can retrieve a game by its bggId
    Given we have performed a GraphQL query 'mutation{gameCreate(game:{title:"Crossbows and Catapults", bggId:2129}) {id}}'
    When we perform a GraphQL query "query{gameByBggId(bggId:2129){title, bggId}}"
    Then the result of "$.data.gameByBggId" will be '[title:Crossbows and Catapults, bggId:2129]'


  @retrieve
  Scenario: We can retrieve a game by its title
    Given we have performed a GraphQL query 'mutation{gameCreate(game:{title:"Crossbows and Catapults", bggId:2129}) {id}}'
    When we perform a GraphQL query 'query{gameByTitle(title:"Crossbows and Catapults"){title, bggId}}'
    Then the result of "$.data.gameByTitle" will be '[title:Crossbows and Catapults, bggId:2129]'
