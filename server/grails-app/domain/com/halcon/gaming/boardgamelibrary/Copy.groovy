package com.halcon.gaming.boardgamelibrary

import org.grails.gorm.graphql.entity.dsl.GraphQLMapping
import org.grails.gorm.graphql.fetcher.impl.PaginatedEntityDataFetcher

import grails.gorm.DetachedCriteria
import graphql.schema.DataFetchingEnvironment

class Copy {
    static graphql = GraphQLMapping.lazy {
        property('uuid', nullable: true)
        query("pagedCopyList", pagedResult(Copy)) {
            argument('owner', String) { nullable true }
            dataFetcher(new PaginatedEntityDataFetcher(Copy.gormPersistentEntity) {
                @Override
                protected DetachedCriteria buildCriteria(DataFetchingEnvironment environment) {
                    String ownerUsername = environment.getArgument('owner')
                    if(ownerUsername == null) {
                        return super.buildCriteria(environment);
                    } else {
                        return Copy.where{owner == User.findByUsername(ownerUsername)}
                    }
                }
            })
        }
    }

    String uuid = UUID.randomUUID().toString()
    User owner
    Title title

    static constraints = {
        uuid nullable:false, validator: {
            try {
                return UUID.fromString(it).toString().equals(it)
            } catch (IllegalArgumentException e) {
                return false
            }
            return true
        }

        owner nullable:false
        title nullable:false
    }
}
