package com.halcon.gaming.boardgamelibrary.cucumber

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

import static org.junit.jupiter.api.Assertions.*

public class StepDefinitions {
    String accessToken
    Integer requestResponseCode
    String requestResponseText

    private static URLConnection performGetRequest(String url, String accessToken = null) {
        URLConnection request = new URL(url).openConnection()
        request.setInstanceFollowRedirects(false)
        if(accessToken != null) {
            request.setRequestProperty("Authorization", "Bearer " + accessToken)
        }
        return request
    }

    @When("we perform a GET request on {string}")
    public void we_perform_a_get_request_on(String url) {
        URLConnection request = performGetRequest(url, accessToken)
        requestResponseCode = request.getResponseCode()
        requestResponseText = requestResponseCode != 404 ? request.getInputStream().getText() : null
    }

    @Then("we get a {int} response")
    public void we_get_a_response(Integer expectedResponseCode) {
        assertEquals(expectedResponseCode, requestResponseCode)
    }
}
