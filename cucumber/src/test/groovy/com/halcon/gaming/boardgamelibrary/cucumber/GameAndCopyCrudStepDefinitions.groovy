package com.halcon.gaming.boardgamelibrary.cucumber

import io.cucumber.java.en.Given
import io.cucumber.java.en.When

class GameAndCopyCrudStepDefinitions {
    private final UserRepository userRepository
    private final GameRepository gameRepository

    private final RestClient restClient

    GameAndCopyCrudStepDefinitions(UserRepository userRepository, GameRepository gameRepository, RestClient restClient) {
        this.userRepository = userRepository
        this.gameRepository = gameRepository
        this.restClient = restClient
    }

    @Given("we have created a game with title {string}")
    public void we_have_created_a_game_with_title(String title) {
        String gameId = RestClient.jsonPathParse(restClient.performGraphQL("mutation{gameCreate(game:{title:\"${title}\"}) {id}}"), '$.data.gameCreate.id')
        gameRepository.registerGame(gameId, title)
    }

    @Given("we have created a game with title {string} and bggid {int}")
    public void we_have_created_a_game_with_title_and_bggid(String title, Integer bggId) {
        restClient.authenticate("admin@example.com", userRepository.userPassword("admin@example.com"))
        String gameId = RestClient.jsonPathParse(restClient.performGraphQL("mutation{gameCreate(game:{title:\"${title}\" bggId:${bggId}}) {id}}"), '$.data.gameCreate.id')
        gameRepository.registerGame(gameId, title)
    }

    @Given("we have created a copy of {string} belonging to {string}")
    @When("we create a copy of {string} belonging to {string}")
    public void we_create_a_copy_of_belonging_to(String title, String username) {
        restClient.graphQL("mutation{copyCreate(copy:{game:{id:${gameRepository.gameId(title)}}, owner:{id:${userRepository.userId(username)}}}) {id, game {title}, owner {username}}}")
    }
}
