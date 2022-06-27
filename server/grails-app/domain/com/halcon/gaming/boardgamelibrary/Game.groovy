package com.halcon.gaming.boardgamelibrary

class Game {
    static graphql = true

    String title
    Long bggId

    static constraints = {
        title nullable:false
        bggId nullable:true
    }
}
