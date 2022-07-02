package com.halcon.gaming.boardgamelibrary

import grails.testing.gorm.DataTest
import graphql.parser.Parser
import spock.lang.Specification

class MyGraphQLFetcherInterceptorSpec extends Specification implements DataTest {

    Class<?>[] getDomainClassesToMock(){
        return [ User, Authority, UserAuthority ] as Class[]
    }

    def "test only ROLE_ADMIN is permitted to query the userList"() {
        given:
        Authority userAuthority = [authority:authority] as Authority
        userAuthority.save(flush:true)
        User currentUser = [username:"test@example.com", password:"password3"] as User
        currentUser.save(flush:true)
        UserAuthority.create(currentUser, userAuthority, true)

        expect:
        new MyGraphQLFetcherInterceptor().permitOperation(currentUser, Parser.parse(document)) == permitted

        where:
        document                    | authority        | permitted
        "query{userList{username}}" | "ROLE_ADMIN"     | true
        "query{userList{username}}" | "ROLE_COMMITTEE" | false
    }

    def "test any user is permitted to retrieve their own id"() {
        given:
        User currentUser = [username:"test@example.com", password:"password3"] as User
        currentUser.save(flush:true)

        expect:
        new MyGraphQLFetcherInterceptor().permitOperation(currentUser, Parser.parse("query{userByUsername(username:\"${currentUser.username}\"){id}}"))
    }

    def "test any user is permitted to update their own password"() {
        given:
        User currentUser = [username:"test@example.com", password:"password3"] as User
        currentUser.save(flush:true)

        expect:
        new MyGraphQLFetcherInterceptor().permitOperation(currentUser, Parser.parse("mutation{userUpdate(id:${currentUser.id}, user:{password:\"test\"}) {id}}"))
    }

    def "test any user can create a title"() {
        given:
        Authority userAuthority = [authority:authority] as Authority
        userAuthority.save(flush:true)
        User currentUser = [username:"test@example.com", password:"password3"] as User
        currentUser.save(flush:true)
        UserAuthority.create(currentUser, userAuthority, true)

        expect:
        new MyGraphQLFetcherInterceptor().permitOperation(currentUser, Parser.parse(document)) == permitted

        where:
        document                                                | authority            | permitted
        'mutation{titleCreate(title:{name:"Some Title"}) {id}}' | "ROLE_ADMIN"         | true
        'mutation{titleCreate(title:{name:"Some Title"}) {id}}' | "ROLE_COMMITTEE"     | true
    }    

    def "test a user is required to create a title"() {
        expect:
        new MyGraphQLFetcherInterceptor().permitOperation(null, Parser.parse('mutation{titleCreate(title:{name:"Some Title"}) {id}}')) == false
    }    
}
