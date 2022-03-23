package com.halcon.gaming.boardgamelibrary;

import org.grails.gorm.graphql.interceptor.manager.GraphQLInterceptorManager
import org.grails.gorm.graphql.plugin.GraphQLPostProcessor

class MyGraphQLCustomizer extends GraphQLPostProcessor {

    @Override
    public void doWith(GraphQLInterceptorManager interceptorManager) {
        interceptorManager.registerInterceptor(Object, new MyGraphQLFetcherInterceptor())
    }
}