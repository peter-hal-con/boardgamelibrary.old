package com.halcon.gaming.boardgamelibrary

import graphql.parser.Parser
import spock.lang.Specification

class GraphQLFieldWrapperSpec extends Specification {

    void testFieldsHaveNames() {
        given:
        GraphQLFieldWrapper wrapper = GraphQLFieldWrapper.wrap(Parser.parse(document))[0]

        expect:
        wrapper.fieldName == fieldName

        where:
        document                                                   | fieldName
        "query{userList{id username}}"                             | "userList"
        "query{userByUsername(username:\"test@example.com\"){id}}" | "userByUsername"
    }

    void testFieldsHaveSelections() {
        given:
        GraphQLFieldWrapper wrapper = GraphQLFieldWrapper.wrap(Parser.parse(document))[0]

        expect:
        wrapper.selectionSet.size() == size
        wrapper.selectionSet.contains(key)

        where:
        document                                                   | size | key
        "query{userList{id username}}"                             | 2    | "id"
        "query{userList{id username}}"                             | 2    | "username"
        "query{userByUsername(username:\"test@example.com\"){id}}" | 1    | "id"
        "query{userList {username authorities {authority}}}"       | 2    | "username"
        "query{userList {username authorities {authority}}}"       | 2    | "authorities.authority"
    }

    void testFieldsHaveArguments() {
        given:
        GraphQLFieldWrapper wrapper = GraphQLFieldWrapper.wrap(Parser.parse(document))[0]

        expect:
        wrapper.argumentMap.size() == size
        wrapper.argumentMap.containsKey(key)
        wrapper.argumentMap[key].value == value

        where:
        document                                                         | size | key             | value
        "query{userByUsername(username:\"test@example.com\"){id}}"       | 1    | "username"      | "test@example.com"
        "mutation{copyCreate(copy:{title:{id: 1}, owner:{id: 2}}) {id}}" | 2    | "copy.title.id" | new BigInteger("1", 10)
        "mutation{copyCreate(copy:{title:{id: 1}, owner:{id: 2}}) {id}}" | 2    | "copy.owner.id" | new BigInteger("2", 10)
    }
}
