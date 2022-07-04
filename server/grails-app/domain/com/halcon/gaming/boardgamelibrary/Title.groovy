package com.halcon.gaming.boardgamelibrary

import org.grails.gorm.graphql.entity.dsl.GraphQLMapping
import org.grails.gorm.graphql.fetcher.impl.SingleEntityDataFetcher

import grails.gorm.DetachedCriteria
import graphql.schema.DataFetchingEnvironment

class Title {
    static graphql = GraphQLMapping.lazy {
        query('titleByBggId', Title) {
            argument("bggId", Integer)
            dataFetcher(new SingleEntityDataFetcher(Title.gormPersistentEntity) {
                @Override
                protected DetachedCriteria buildCriteria(DataFetchingEnvironment environment) {
                    Title.where { bggId == environment.getArgument('bggId') }
                }
            })
        }
        query('titleByName', Title) {
            argument("name", String)
            dataFetcher(new SingleEntityDataFetcher(Title.gormPersistentEntity) {
                @Override
                protected DetachedCriteria buildCriteria(DataFetchingEnvironment environment) {
                    Title.where { name == environment.getArgument('name') }
                }
            })
        }
    }

    String name
    Long bggId

    static constraints = {
        name nullable:false
        bggId nullable:true, unique:true
    }
}
