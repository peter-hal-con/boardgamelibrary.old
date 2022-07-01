package com.halcon.gaming.boardgamelibrary.cucumber

import static org.awaitility.Awaitility.*
import static org.junit.jupiter.api.Assertions.*

import java.util.concurrent.TimeUnit

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

public class StepDefinitions {
    private final UserRepository userRepository
    private final RestClient restClient

    StepDefinitions(UserRepository userRepository, RestClient restClient) {
        this.userRepository = userRepository
        this.restClient = restClient
    }

    @Given("the following users exist:")
    public void the_following_users_exist(io.cucumber.datatable.DataTable dataTable) {
        dataTable.asMaps().each {
            def authorities = it.authorities != null ? it.authorities.split(',') : []
            def request = RestClient.performPostRequest("/testOnly/createUser", [username:it.username, password:it.password, authorities:authorities])
            assertEquals(200, request.getResponseCode())
            userRepository.registerUser(RestClient.jsonPathParse(request.getInputStream().getText(), '$.id').toString(), it.username, it.password)
        }
    }

    @Given("we are authenticated as {string}")
    public void we_are_authenticated_as(String username) {
        restClient.authenticate(username, userRepository.userPassword(username))
    }

    @When("we perform a GET request on {string}")
    public void we_perform_a_get_request_on(String path) {
        restClient.GET(path)
    }

    @When("we authenticate as {string} with password {string}")
    public void we_authenticate_as_with_password(String username, String password) {
        restClient.authenticate(username, password)
    }

    @Then("we get a {int} response")
    public void we_get_a_response(Integer expectedResponseCode) {
        assertEquals(expectedResponseCode, restClient.responseCode)
    }

    @Then("we will have an access token")
    public void we_will_have_an_access_token() {
        assertTrue(restClient.hasAccessToken())
    }

    @Then("we will not have an access token")
    public void we_will_not_have_an_access_token() {
        assertFalse(restClient.hasAccessToken())
    }

    @Given("we have performed a GraphQL query {string}")
    @When("we perform a GraphQL query {string}")
    public void we_perform_a_graph_ql_query(String query) {
        restClient.graphQL(query)
    }

    @Then("the result of {string} will be {string}")
    public void the_result_of_will_be(String jsonPath, String expectedValue) {
        assertEquals(expectedValue, restClient.extractJsonPathFromResponse(jsonPath).toString())
    }

    @Then("the result of {string} will not be {string}")
    public void the_result_of_will_not_be(String jsonPath, String unexpectedValue) {
        assertNotEquals(unexpectedValue, restClient.extractJsonPathFromResponse(jsonPath).toString())
    }

    @Then("the result of {string} will have a value")
    public void the_result_of_will_have_a_value(String jsonPath) {
        assertNotNull(restClient.extractJsonPathFromResponse(jsonPath).toString())
    }

    @Then("there will be a game with the name {string}")
    public void there_will_be_a_game_with_the_name(String title) {
        await().atMost(5, TimeUnit.SECONDS).until {
            restClient.authenticate("admin@example.com", userRepository.userPassword("admin@example.com"))
            restClient.graphQL("query{gameByTitle(title:\"${title}\"){title}}")
            return title == restClient.extractJsonPathFromResponse('$.data.gameByTitle.title')
        }
    }

    @Then("there will be a copy of the game named {string} belonging to {string}")
    public void there_will_be_a_copy_of_the_game_named_belonging_to(String title, String username) {
        await().atMost(5, TimeUnit.SECONDS).until {
            restClient.authenticate("admin@example.com", userRepository.userPassword("admin@example.com"))
            restClient.graphQL("query{copyList{game{title} owner{username}}}")
            return title == restClient.extractJsonPathFromResponse('$.data.copyList[0].game.title') &&
                    username == restClient.extractJsonPathFromResponse('$.data.copyList[0].owner.username')
        }
    }
}
