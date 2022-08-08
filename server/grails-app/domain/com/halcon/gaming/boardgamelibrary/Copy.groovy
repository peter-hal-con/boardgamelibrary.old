package com.halcon.gaming.boardgamelibrary

import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment

class Copy {
    static graphql = GraphQLMapping.build {
        property('uuid', nullable: true)
        query("copyList", [Copy]) {
            argument('owner', String) { nullable true }
            dataFetcher(new DataFetcher() {
                @Override
                public Object get(DataFetchingEnvironment environment) {
                    String ownerUsername = environment.getArgument('owner')
                    if(ownerUsername == null) {
                        return Copy.list()
                    } else {
                        User owner = User.findByUsername(ownerUsername)
                        return Copy.findAllByOwner(owner)
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
