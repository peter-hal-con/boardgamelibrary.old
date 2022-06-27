package com.halcon.gaming.boardgamelibrary

import org.grails.gorm.graphql.fetcher.GraphQLDataFetcherType
import org.grails.gorm.graphql.interceptor.GraphQLFetcherInterceptor

import grails.util.Environment
import grails.util.Holders
import graphql.language.Argument
import graphql.language.Document
import graphql.language.IntValue
import graphql.language.ObjectValue
import graphql.language.SelectionSet
import graphql.language.StringValue
import graphql.language.OperationDefinition.Operation
import graphql.schema.DataFetchingEnvironment

class MyGraphQLFetcherInterceptor implements GraphQLFetcherInterceptor {
    static User getCurrentUser() {
        Holders.applicationContext.springSecurityService.currentUser
    }

    static boolean isRunningInDevelopmentEnvironment() {
        return Environment.current == Environment.DEVELOPMENT
    }

    static boolean isAdminUser(User user) {
        return user != null && user.authorities.contains(Authority.findByAuthority("ROLE_ADMIN"))
    }

    static boolean isOperationWithName(def node, Operation operation, String name) {
        return  node.operation == operation &&
                node.children.size() == 1 &&
                node.children[0].selections.size() == 1 &&
                node.children[0].selections[0].name == name
    }

    static boolean isUserByUsername(Document document) {
        return  document.children.size() == 1 &&
                isOperationWithName(document.children[0], Operation.QUERY, "userByUsername")
    }

    static boolean isUserUpdate(Document document) {
        return  document.children.size() == 1 &&
                isOperationWithName(document.children[0], Operation.MUTATION, "userUpdate")
    }

    static Argument findArgumentByName(List<Argument> arguments, String name) {
        for (argument in arguments) {
            if(argument.name == name) return argument
        }
        return null
    }

    static boolean isTargetingCurrentUserByUsername(Document document, User currentUser) {
        return findArgumentByName(document.children[0].children[0].selections[0].arguments, 'username').value.value == currentUser.username
    }

    static boolean isTargetingCurrentUserById(Document document, User currentUser) {
        return findArgumentByName(document.children[0].children[0].selections[0].arguments, 'id').value.value == currentUser.id
    }

    static boolean isOnlyRetrievingId(Document document) {
        SelectionSet selectionSet = document.children[0].children[0].selections[0].selectionSet
        return  selectionSet.selections.size() == 1 &&
                selectionSet.selections[0].name == 'id'
    }

    static boolean isOnlyChangingPassword(Document document) {
        Argument userArgument = findArgumentByName(document.children[0].children[0].selections[0].arguments, 'user')
        return  userArgument.value instanceof ObjectValue &&
                userArgument.value.objectFields.size() == 1 &&
                userArgument.value.objectFields[0].name == 'password'
    }

    static boolean isCurrentUserRetrievingTheirOwnId(User currentUser, Document document) {
        return  isUserByUsername(document) &&
                isTargetingCurrentUserByUsername(document, currentUser) &&
                isOnlyRetrievingId(document)
    }

    static boolean isCurrentUserChangingTheirOwnPassword(User currentUser, Document document) {
        return  isUserUpdate(document) &&
                isTargetingCurrentUserById(document, currentUser) &&
                isOnlyChangingPassword(document)
    }

    static boolean isUserCreatingGame(User currentUser, Document document) {
        return  currentUser != null &&
                document.children.size() == 1 &&
                isOperationWithName(document.children[0], Operation.MUTATION, "gameCreate")
    }

    boolean permitOperation(User currentUser, Document document) {
        return  isRunningInDevelopmentEnvironment() ||
                isAdminUser(currentUser) ||
                isCurrentUserRetrievingTheirOwnId(currentUser, document) ||
                isCurrentUserChangingTheirOwnPassword(currentUser, document) ||
                isUserCreatingGame(currentUser, document)
    }

    @Override
    boolean onQuery(DataFetchingEnvironment environment, GraphQLDataFetcherType type) {
        return permitOperation(currentUser, environment.document)
    }

    @Override
    boolean onMutation(DataFetchingEnvironment environment, GraphQLDataFetcherType type) {
        return permitOperation(currentUser, environment.document)
    }

    @Override
    boolean onCustomQuery(String name, DataFetchingEnvironment environment) {
        return permitOperation(currentUser, environment.document)
    }

    @Override
    boolean onCustomMutation(String name, DataFetchingEnvironment environment) {
        return permitOperation(currentUser, environment.document)
    }
}
