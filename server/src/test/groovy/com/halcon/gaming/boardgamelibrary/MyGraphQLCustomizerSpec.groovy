package com.halcon.gaming.boardgamelibrary

import org.grails.gorm.graphql.interceptor.manager.GraphQLInterceptorManager

import spock.lang.Specification

class MyGraphQLCustomizerSpec extends Specification {

    def "test MyGraphQLCustomizer installs MyGraphQLFetcherInterceptor"() {
        given:
        def interceptorManager = Mock(GraphQLInterceptorManager.class)

        when:
        new MyGraphQLCustomizer().doWith(interceptorManager)

        then:
        1 * interceptorManager.registerInterceptor(Object, _)
    }
}
