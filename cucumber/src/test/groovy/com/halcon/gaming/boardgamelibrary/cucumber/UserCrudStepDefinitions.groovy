package com.halcon.gaming.boardgamelibrary.cucumber

import static org.awaitility.Awaitility.*
import static org.junit.jupiter.api.Assertions.*

import java.util.concurrent.TimeUnit

import io.cucumber.java.en.Then
import io.cucumber.java.en.When

class UserCrudStepDefinitions {
    private final Map<String, String> authorityIdMap = ["ROLE_ADMIN":"1", "ROLE_COMMITTEE":"2"]

    private final UserRepository userRepository
    private final RestClient restClient

    UserCrudStepDefinitions(UserRepository userRepository, RestClient restClient) {
        this.userRepository = userRepository
        this.restClient = restClient
    }

    private String createUser(String username, String password) {
        return restClient.performGraphQL("mutation{userCreate(user:{username:\"${username}\" password:\"${password}\" accountLocked:false accountExpired:false passwordExpired:false enabled:true}){id}}")
    }

    private void grantAuthorityToUser(def userId, def authorityId) {
        def responseText = restClient.performGraphQL("mutation{userAuthorityCreate(userAuthority:{user:{id:${userId}} authority:{id:${authorityId}}}){errors{message}}}")
        assertEquals("[]", RestClient.jsonPathParse(responseText, '$.data.userAuthorityCreate.errors').toString())
    }

    @When("we create a user with username {string} and password {string}")
    public void we_create_a_user_with_username_and_password(String username, String password) {
        userRepository.registerUser(RestClient.jsonPathParse(createUser(username, password), '$.data.userCreate.id').toString(), username, password)
    }

    @When("we create a user with username {string} and password {string} and authority {string}")
    public void we_create_a_user_with_username_and_password_and_authority(String username, String password, String authority) {
        we_create_a_user_with_username_and_password(username, password)
        grantAuthorityToUser(userRepository.userId(username), authorityIdMap[authority])
    }

    @When("we retrieve the user with username {string}")
    public void we_retrieve_the_user_with_username(String username) {
        restClient.graphQL("query{user(id:${userRepository.userId(username)}){username authorities{authority}}}")
    }

    @When("we update the user with username {string} so that their {string} is {string}")
    public void we_update_the_user_with_username_so_that_their_is(String username, String field, String newValue) {
        restClient.performGraphQL("mutation{userUpdate(id:${userRepository.userId(username)} user:{${field}:\"${newValue}\"}){id}}")
    }

    @When("we grant the {string} authority to the user with username {string}")
    public void we_grant_the_authority_to_the_user_with_username(String authority, String username) {
        grantAuthorityToUser(userRepository.userId(username), authorityIdMap[authority])
    }

    @When("we revoke the {string} authority from the user with username {string}")
    public void we_revoke_the_authority_from_the_user_with_username(String authority, String username) {
        def responseText = restClient.performGraphQL("mutation{userAuthorityDelete(user:${userRepository.userId(username)} authority:${authorityIdMap[authority]}){success}}")
        assertEquals("true", RestClient.jsonPathParse(responseText, '$.data.userAuthorityDelete.success').toString())
    }

    @When("we delete the user with username {string}")
    public void we_delete_the_user_with_username(String username) {
        def userId = userRepository.userId(username)
        def authorityIds = RestClient.jsonPathParse(restClient.performGraphQL("query{user(id:${userId}){authorities{id}}}"), '$..id')
        def userAuthorityDeletes = authorityIds.collect {
            "userAuthorityDelete(user:${userId}, authority:${it}){success}"
        }.join(' ')
        def responseText = restClient.performGraphQL("mutation{${userAuthorityDeletes} userDelete(id:${userId}){success}}")
        assertEquals("true", RestClient.jsonPathParse(responseText, '$.data.userDelete.success').toString())
    }

    @Then("the user with username {string} will have the password {string}")
    public void the_user_with_username_will_have_the_password(String username, String password) {
        await().atMost(5, TimeUnit.SECONDS).until {
            RestClient.performAuthentication(username, password) != null
        }
    }

    @Then("the user with username {string} will have the authority {string}")
    public void the_user_with_username_will_have_the_authority(String username, String authority) {
        await().atMost(5, TimeUnit.SECONDS).until {
            restClient.authenticate("admin@example.com", userRepository.userPassword("admin@example.com"))
            restClient.graphQL("query{userByUsername(username:\"${username}\"){authorities{authority}}}")
            restClient.extractJsonPathFromResponse('$.data.userByUsername.authorities[*].authority').contains(authority)
        }
    }

    @Then("the user with username {string} will not have the authority {string}")
    public void the_user_with_username_will_not_have_the_authority(String username, String authority) {
        await().atMost(5, TimeUnit.SECONDS).until {
            restClient.authenticate("admin@example.com", userRepository.userPassword("admin@example.com"))
            restClient.graphQL("query{userByUsername(username:\"${username}\"){authorities{authority}}}")
            !restClient.extractJsonPathFromResponse('$.data.userByUsername.authorities[*].authority').contains(authority)
        }
    }
}
