package com.halcon.gaming.boardgamelibrary.cucumber

import io.cucumber.java.en.Given
import io.cucumber.java.en.When

class TitleAndCopyCrudStepDefinitions {
    private final UserRepository userRepository
    private final TitleRepository titleRepository

    private final RestClient restClient

    TitleAndCopyCrudStepDefinitions(UserRepository userRepository, TitleRepository titleRepository, RestClient restClient) {
        this.userRepository = userRepository
        this.titleRepository = titleRepository
        this.restClient = restClient
    }

    @Given("we have created a title with name {string}")
    public void we_have_created_a_title_with_name(String name) {
        String titleId = RestClient.jsonPathParse(restClient.performGraphQL("mutation{titleCreate(title:{name:\"${name}\"}) {id}}"), '$.data.titleCreate.id')
        titleRepository.registerTitle(titleId, name)
    }

    @Given("we have created a title with name {string} and bggid {int}")
    public void we_have_created_a_title_with_name_and_bggid(String name, Integer bggId) {
        restClient.authenticate("admin@example.com", userRepository.userPassword("admin@example.com"))
        String titleId = RestClient.jsonPathParse(restClient.performGraphQL("mutation{titleCreate(title:{name:\"${name}\" bggId:${bggId}}) {id}}"), '$.data.titleCreate.id')
        titleRepository.registerTitle(titleId, name)
    }

    @Given("we have created a copy of {string} belonging to {string}")
    @When("we create a copy of {string} belonging to {string}")
    public void we_create_a_copy_of_belonging_to(String title, String username) {
        restClient.graphQL("mutation{copyCreate(copy:{title:{id:${titleRepository.titleId(title)}}, owner:{id:${userRepository.userId(username)}}}) {id, title {name}, owner {username}}}")
    }
}
