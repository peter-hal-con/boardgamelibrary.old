package com.halcon.gaming.boardgamelibrary.cucumber

import static org.junit.jupiter.api.Assertions.*

import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.PathNotFoundException

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

class RestClient {
    private String accessToken
    private Integer requestResponseCode
    private String requestResponseText

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

    private static String performAuthentication(String username, String password) {
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

    public void authenticate(String username, String password) {
        accessToken = performAuthentication(username, password)
    }

    public void GET(String path) {
        URLConnection request = RestClient.performGetRequest(path, accessToken)
        requestResponseCode = request.getResponseCode()
        requestResponseText = requestResponseCode == 200 ? request.getInputStream().getText() : null
    }

    public getResponseCode() {
        return requestResponseCode
    }

    public hasAccessToken() {
        return accessToken != null
    }

    public String performGraphQL(String query) {
        def request = performGetRequest("/graphql?query=${URLEncoder.encode(query, 'UTF-8')}", accessToken)
        assertEquals(200, request.responseCode)
        return request.getInputStream().getText()
    }

    public void graphQL(String query) {
        GET("/graphql?query=${URLEncoder.encode(query, 'UTF-8')}")
    }

    public def extractJsonPathFromResponse(String jsonPath) {
        jsonPathParse(requestResponseText, jsonPath)
    }
}
