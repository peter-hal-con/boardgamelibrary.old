package com.halcon.gaming.boardgamelibrary

import org.grails.gorm.graphql.fetcher.GraphQLDataFetcherType
import org.grails.gorm.graphql.interceptor.GraphQLFetcherInterceptor

import grails.util.Environment
import grails.util.Holders
import graphql.language.Document
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

    static boolean isCommitteeUser(User user) {
        return user != null && user.authorities.contains(Authority.findByAuthority("ROLE_COMMITTEE"))
    }

    static boolean isCurrentUserRetrievingTheirOwnId(User currentUser, GraphQLFieldWrapper wrapper) {
        return  wrapper.fieldName == "userByUsername" &&
                wrapper.argumentMap["username"].value == currentUser.username &&
                wrapper.selectionSet.size() == 1 && wrapper.selectionSet.contains("id")
    }

    static boolean isCurrentUserChangingTheirOwnPassword(User currentUser, GraphQLFieldWrapper wrapper) {
        return  wrapper.fieldName == "userUpdate" &&
                wrapper.argumentMap.size() == 2 &&
                wrapper.argumentMap["id"].value.longValue() == currentUser.id &&
                wrapper.argumentMap.containsKey("user.password") &&
                wrapper.selectionSet.size() == 1 && wrapper.selectionSet.contains("id")
    }

    static boolean isCommitteeUserCreatingTitle(User currentUser, GraphQLFieldWrapper wrapper) {
        return  isCommitteeUser(currentUser) &&
                wrapper.fieldName == "titleCreate"
    }

    static boolean isCommitteeUserCreatingTheirOwnCopy(User currentUser, GraphQLFieldWrapper wrapper) {
        return  isCommitteeUser(currentUser) &&
                wrapper.fieldName == "copyCreate" &&
                wrapper.argumentMap["copy.owner.id"].value.longValue() == currentUser.id
    }

    static boolean isCommitteeUserRetrievingTheirOwnCopies(User currentUser, GraphQLFieldWrapper wrapper) {
        return  isCommitteeUser(currentUser) &&
                wrapper.fieldName == "pagedCopyList" &&
                wrapper.argumentMap["owner"]?.value == currentUser.username
    }

    boolean permitOperation(User currentUser, Document document) {
        if(isRunningInDevelopmentEnvironment() || isAdminUser(currentUser)) return true

        boolean permit = true

        for(GraphQLFieldWrapper wrapper : GraphQLFieldWrapper.wrap(document)) {
            permit = permit && currentUser != null && (isCurrentUserRetrievingTheirOwnId(currentUser, wrapper) ||
                    isCurrentUserChangingTheirOwnPassword(currentUser, wrapper) ||
                    isCommitteeUserCreatingTitle(currentUser, wrapper) ||
                    isCommitteeUserCreatingTheirOwnCopy(currentUser, wrapper) ||
                    isCommitteeUserRetrievingTheirOwnCopies(currentUser, wrapper))
        }

        return permit
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
