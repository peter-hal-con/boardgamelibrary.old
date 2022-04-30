package com.halcon.gaming.boardgamelibrary

import org.grails.gorm.graphql.fetcher.GraphQLDataFetcherType
import org.grails.gorm.graphql.interceptor.GraphQLFetcherInterceptor

import grails.util.Environment
import grails.util.Holders
import graphql.language.Document
import graphql.schema.DataFetchingEnvironment

class MyGraphQLFetcherInterceptor implements GraphQLFetcherInterceptor {
    boolean permitOperation(User currentUser, Document document) {
        return Environment.current == Environment.DEVELOPMENT || currentUser.authorities.contains(Authority.findByAuthority("ROLE_ADMIN"))
    }

    User getCurrentUser() {
        Holders.applicationContext.springSecurityService.currentUser
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
