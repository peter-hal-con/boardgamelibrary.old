package com.halcon.gaming.boardgamelibrary

import org.grails.gorm.graphql.entity.dsl.GraphQLMapping
import org.grails.gorm.graphql.fetcher.impl.SingleEntityDataFetcher

import grails.gorm.DetachedCriteria
import graphql.schema.DataFetchingEnvironment

class Game {
    static graphql = GraphQLMapping.lazy {
        query('gameByBggId', Game) {
            argument("bggId", Integer)
            dataFetcher(new SingleEntityDataFetcher(Game.gormPersistentEntity) {
                @Override
                protected DetachedCriteria buildCriteria(DataFetchingEnvironment environment) {
                    Game.where { bggId == environment.getArgument('bggId') }
                }
            })
        }
    }

    String title
    Long bggId

    static constraints = {
        title nullable:false
        bggId nullable:true
    }
}
