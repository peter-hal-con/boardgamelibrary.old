package com.halcon.gaming.boardgamelibrary

import grails.testing.gorm.DataTest
import graphql.parser.Parser
import spock.lang.Specification

class MyGraphQLFetcherInterceptorSpec extends Specification implements DataTest {

    Class<?>[] getDomainClassesToMock(){
        return [ User, Authority, UserAuthority ] as Class[]
    }

    def "test MyGraphQLFetcherInterceptor"() {
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
}
