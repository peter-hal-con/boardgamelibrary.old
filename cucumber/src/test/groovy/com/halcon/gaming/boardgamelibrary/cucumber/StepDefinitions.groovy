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

    String accessToken
    Integer requestResponseCode
    String requestResponseText

    StepDefinitions(UserRepository userRepository) {
        this.userRepository = userRepository
    }

    private static openConnection(String path) {
        return new URL("http://localhost:8080${path}").openConnection()
    }

    private static URLConnection performGetRequest(String path, String accessToken = null) {
        URLConnection request = openConnection(path)
        request.setInstanceFollowRedirects(false)
        if(accessToken != null) {
            request.setRequestProperty("Authorization", "Bearer " + accessToken)
        }
        return request
    }

    private static URLConnection performPostRequest(String path, def body) {
        URLConnection request = openConnection(path)
        request.setRequestMethod("POST")
        request.setDoOutput(true)
        request.setRequestProperty("Content-Type", "application/json")
        request.getOutputStream().write(JsonOutput.toJson(body).getBytes("UTF-8"))
        return request
    }

    private static String authenticate(String username, String password) {
        def request = performPostRequest("/api/login", [username:username, password:password])
        return request.responseCode == 200 ? new JsonSlurper().parseText(request.getInputStream().getText()).access_token : null
    }

    private static def jsonPathParse(String document, String jsonPath) {
        try {
            return JsonPath.parse(document).read(jsonPath)
        } catch(PathNotFoundException e) {
            println("--- PathNotFoundException ---")
            println("document: ${document}")
            println("jsonPath: ${jsonPath}")
            println("-----------------------------")
            fail()
        }
    }

    @Given("the following users exist:")
    public void the_following_users_exist(io.cucumber.datatable.DataTable dataTable) {
        dataTable.asMaps().each {
            def authorities = it.authorities != null ? it.authorities.split(',') : []
            def request = performPostRequest("/testOnly/createUser", [username:it.username, password:it.password, authorities:authorities])
            assertEquals(200, request.getResponseCode())
            userRepository.registerUser(jsonPathParse(request.getInputStream().getText(), '$.id').toString(), it.username, it.password)
        }
    }

    @Given("we are authenticated as {string}")
    public void we_are_authenticated_as(String username) {
        accessToken = authenticate(username, userRepository.userPassword(username))
    }

    @When("we perform a GET request on {string}")
    public void we_perform_a_get_request_on(String path) {
        URLConnection request = performGetRequest(path, accessToken)
        requestResponseCode = request.getResponseCode()
        requestResponseText = requestResponseCode != 404 ? request.getInputStream().getText() : null
    }

    @When("we authenticate as {string} with password {string}")
    public void we_authenticate_as_with_password(String username, String password) {
        accessToken = authenticate(username, password)
    }

    @Then("we get a {int} response")
    public void we_get_a_response(Integer expectedResponseCode) {
        assertEquals(expectedResponseCode, requestResponseCode)
    }

    @Then("we will have an access token")
    public void we_will_have_an_access_token() {
        assertNotNull(accessToken)
    }

    @Then("we will not have an access token")
    public void we_will_not_have_an_access_token() {
        assertNull(accessToken)
    }
}
