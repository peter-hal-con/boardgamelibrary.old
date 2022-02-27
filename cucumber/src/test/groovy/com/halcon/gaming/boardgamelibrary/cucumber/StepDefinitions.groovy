package com.halcon.gaming.boardgamelibrary.cucumber

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

import static org.junit.jupiter.api.Assertions.*

import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.PathNotFoundException

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

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
}
