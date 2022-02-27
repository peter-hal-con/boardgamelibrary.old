package com.halcon.gaming.boardgamelibrary

import org.grails.gorm.graphql.entity.dsl.GraphQLMapping
import org.grails.gorm.graphql.fetcher.impl.SingleEntityDataFetcher

import grails.gorm.DetachedCriteria
import graphql.schema.DataFetchingEnvironment
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

//TODO: @GrailsCompileStatic
@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class User implements Serializable {

    private static final long serialVersionUID = 1
    static graphql = GraphQLMapping.lazy {
        add('authorities', [Authority]) {
            dataFetcher { User user ->
                user.authorities
            }
        }
        query('userByUsername', User) {
            argument("username", String)
            dataFetcher(new SingleEntityDataFetcher(User.gormPersistentEntity) {
                @Override
                protected DetachedCriteria buildCriteria(DataFetchingEnvironment environment) {
                    User.where { username == environment.getArgument('username') }
                }
            })
        }
    }

    String username
    String password
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired

    Set<Authority> getAuthorities() {
        (UserAuthority.findAllByUser(this) as List<UserAuthority>)*.authority as Set<Authority>
    }

    static constraints = {
        password nullable: false, blank: false, password: true
        username nullable: false, blank: false, unique: true
    }

    static mapping = {
        password column: '`password`'
    }
}
