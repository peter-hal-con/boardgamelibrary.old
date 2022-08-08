package com.halcon.gaming.boardgamelibrary

import grails.testing.gorm.DataTest
import graphql.parser.Parser
import spock.lang.Shared
import spock.lang.Specification

class MyGraphQLFetcherInterceptorSpec extends Specification implements DataTest {

    Class<?>[] getDomainClassesToMock(){
        return [ User, Authority, UserAuthority ] as Class[]
    }

    @Shared private Authority adminAuthority
    @Shared private Authority committeeAuthority

    @Shared private User adminUser
    @Shared private User committeeUser
    @Shared private User testUser

    private Authority createAuthority(String auth) {
        Authority authority = ([authority:auth] as Authority).save(flish:true)

        return authority
    }

    private User createUser(String username, String password, Authority authority) {
        User user = ([username:username, password:password] as User).save(flush:true)


        if(authority != null) {
            UserAuthority.create(user, authority, true)
        }

        return user
    }

    def setupSpec() {
        adminAuthority = createAuthority("ROLE_ADMIN")
        committeeAuthority = createAuthority("ROLE_COMMITTEE")

        adminUser = createUser("admin@example.com", "password1", adminAuthority)
        committeeUser = createUser("committee@example.com", "password2", committeeAuthority)
        testUser = createUser("test@example.com", "password3", null)
    }

    def setup() {
        setupSpec()
    }

    def "test"() {
        expect:
        new MyGraphQLFetcherInterceptor().permitOperation(user, Parser.parse(document)) == permitted

        where:
        document                                                                                   | user          | permitted
        "query{userList{username}}"                                                                | adminUser     | true
        "query{userList{username}}"                                                                | committeeUser | false
        "query{userList{username}}"                                                                | testUser      | false
        "query{userList{username}}"                                                                | null          | false

        "query{userByUsername(username:\"${adminUser.username}\"){id}}"                            | adminUser     | true
        "query{userByUsername(username:\"${committeeUser.username}\"){id}}"                        | adminUser     | true
        "query{userByUsername(username:\"${testUser.username}\"){id}}"                             | adminUser     | true

        "query{userByUsername(username:\"${adminUser.username}\"){id}}"                            | committeeUser | false
        "query{userByUsername(username:\"${committeeUser.username}\"){id}}"                        | committeeUser | true
        "query{userByUsername(username:\"${testUser.username}\"){id}}"                             | committeeUser | false

        "query{userByUsername(username:\"${adminUser.username}\"){id}}"                            | testUser      | false
        "query{userByUsername(username:\"${committeeUser.username}\"){id}}"                        | testUser      | false
        "query{userByUsername(username:\"${testUser.username}\"){id}}"                             | testUser      | true

        "query{userByUsername(username:\"${adminUser.username}\"){id}}"                            | null          | false
        "query{userByUsername(username:\"${committeeUser.username}\"){id}}"                        | null          | false
        "query{userByUsername(username:\"${testUser.username}\"){id}}"                             | null          | false

        "query{copyList{id, owner{username}, title{name}}}"                                        | adminUser     | true
        "query{copyList{id, owner{username}, title{name}}}"                                        | committeeUser | false
        "query{copyList{id, owner{username}, title{name}}}"                                        | testUser      | false
        "query{copyList{id, owner{username}, title{name}}}"                                        | null          | false
        
        "query{copyList(owner:\"${adminUser.username}\"){id, owner{username}, title{name}}}"       | adminUser     | true
        "query{copyList(owner:\"${committeeUser.username}\"){id, owner{username}, title{name}}}"   | adminUser     | true
        "query{copyList(owner:\"${testUser.username}\"){id, owner{username}, title{name}}}"        | adminUser     | true
        
        "query{copyList(owner:\"${adminUser.username}\"){id, owner{username}, title{name}}}"       | committeeUser | false
        "query{copyList(owner:\"${committeeUser.username}\"){id, owner{username}, title{name}}}"   | committeeUser | true
        "query{copyList(owner:\"${testUser.username}\"){id, owner{username}, title{name}}}"        | committeeUser | false
        
        "query{copyList(owner:\"${adminUser.username}\"){id, owner{username}, title{name}}}"       | testUser      | false
        "query{copyList(owner:\"${committeeUser.username}\"){id, owner{username}, title{name}}}"   | testUser      | false
        "query{copyList(owner:\"${testUser.username}\"){id, owner{username}, title{name}}}"        | testUser      | false

        "mutation{userUpdate(id:${adminUser.id}, user:{password:\"test\"}) {id}}"                  | adminUser     | true
        "mutation{userUpdate(id:${committeeUser.id}, user:{password:\"test\"}) {id}}"              | adminUser     | true
        "mutation{userUpdate(id:${testUser.id}, user:{password:\"test\"}) {id}}"                   | adminUser     | true

        "mutation{userUpdate(id:${adminUser.id}, user:{password:\"test\"}) {id}}"                  | committeeUser | false
        "mutation{userUpdate(id:${committeeUser.id}, user:{password:\"test\"}) {id}}"              | committeeUser | true
        "mutation{userUpdate(id:${testUser.id}, user:{password:\"test\"}) {id}}"                   | committeeUser | false

        "mutation{userUpdate(id:${adminUser.id}, user:{password:\"test\"}) {id}}"                  | testUser      | false
        "mutation{userUpdate(id:${committeeUser.id}, user:{password:\"test\"}) {id}}"              | testUser      | false
        "mutation{userUpdate(id:${testUser.id}, user:{password:\"test\"}) {id}}"                   | testUser      | true

        "mutation{userUpdate(id:${adminUser.id}, user:{password:\"test\"}) {id}}"                  | null          | false
        "mutation{userUpdate(id:${committeeUser.id}, user:{password:\"test\"}) {id}}"              | null          | false
        "mutation{userUpdate(id:${testUser.id}, user:{password:\"test\"}) {id}}"                   | null          | false

        "mutation{titleCreate(title:{name:\"Some Title\"}) {id}}"                                  | adminUser     | true
        "mutation{titleCreate(title:{name:\"Some Title\"}) {id}}"                                  | committeeUser | true
        "mutation{titleCreate(title:{name:\"Some Title\"}) {id}}"                                  | testUser      | false
        "mutation{titleCreate(title:{name:\"Some Title\"}) {id}}"                                  | null          | false

        "mutation{copyCreate(copy:{title:{id: 1}, owner:{id: ${adminUser.id}}}) {id}}"             | adminUser     | true
        "mutation{copyCreate(copy:{title:{id: 1}, owner:{id: ${committeeUser.id}}}) {id}}"         | adminUser     | true
        "mutation{copyCreate(copy:{title:{id: 1}, owner:{id: ${testUser.id}}}) {id}}"              | adminUser     | true

        "mutation{copyCreate(copy:{title:{id: 1}, owner:{id: ${adminUser.id}}}) {id}}"             | committeeUser | false
        "mutation{copyCreate(copy:{title:{id: 1}, owner:{id: ${committeeUser.id}}}) {id}}"         | committeeUser | true
        "mutation{copyCreate(copy:{title:{id: 1}, owner:{id: ${testUser.id}}}) {id}}"              | committeeUser | false

        "mutation{copyCreate(copy:{title:{id: 1}, owner:{id: ${adminUser.id}}}) {id}}"             | testUser      | false
        "mutation{copyCreate(copy:{title:{id: 1}, owner:{id: ${committeeUser.id}}}) {id}}"         | testUser      | false
        "mutation{copyCreate(copy:{title:{id: 1}, owner:{id: ${testUser.id}}}) {id}}"              | testUser      | false

        "mutation{copyCreate(copy:{title:{id: 2}, owner:{id: ${adminUser.id}}}) {id}}"             | adminUser     | true
        "mutation{copyCreate(copy:{title:{id: 2}, owner:{id: ${committeeUser.id}}}) {id}}"         | adminUser     | true
        "mutation{copyCreate(copy:{title:{id: 2}, owner:{id: ${testUser.id}}}) {id}}"              | adminUser     | true

        "mutation{copyCreate(copy:{title:{id: 2}, owner:{id: ${adminUser.id}}}) {id}}"             | committeeUser | false
        "mutation{copyCreate(copy:{title:{id: 2}, owner:{id: ${committeeUser.id}}}) {id}}"         | committeeUser | true
        "mutation{copyCreate(copy:{title:{id: 2}, owner:{id: ${testUser.id}}}) {id}}"              | committeeUser | false

        "mutation{copyCreate(copy:{title:{id: 2}, owner:{id: ${adminUser.id}}}) {id}}"             | testUser      | false
        "mutation{copyCreate(copy:{title:{id: 2}, owner:{id: ${committeeUser.id}}}) {id}}"         | testUser      | false
        "mutation{copyCreate(copy:{title:{id: 2}, owner:{id: ${testUser.id}}}) {id}}"              | testUser      | false

        "mutation{copyCreate(copy:{title:{id: 3}, owner:{id: ${adminUser.id}}}) {id}}"             | adminUser     | true
        "mutation{copyCreate(copy:{title:{id: 3}, owner:{id: ${committeeUser.id}}}) {id}}"         | adminUser     | true
        "mutation{copyCreate(copy:{title:{id: 3}, owner:{id: ${testUser.id}}}) {id}}"              | adminUser     | true

        "mutation{copyCreate(copy:{title:{id: 3}, owner:{id: ${adminUser.id}}}) {id}}"             | committeeUser | false
        "mutation{copyCreate(copy:{title:{id: 3}, owner:{id: ${committeeUser.id}}}) {id}}"         | committeeUser | true
        "mutation{copyCreate(copy:{title:{id: 3}, owner:{id: ${testUser.id}}}) {id}}"              | committeeUser | false

        "mutation{copyCreate(copy:{title:{id: 3}, owner:{id: ${adminUser.id}}}) {id}}"             | testUser      | false
        "mutation{copyCreate(copy:{title:{id: 3}, owner:{id: ${committeeUser.id}}}) {id}}"         | testUser      | false
        "mutation{copyCreate(copy:{title:{id: 3}, owner:{id: ${testUser.id}}}) {id}}"              | testUser      | false
    }
}
