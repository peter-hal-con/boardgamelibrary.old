package com.halcon.gaming.boardgamelibrary

import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

class Copy {
    static graphql = GraphQLMapping.build {
        property('uuid', nullable: true)
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
