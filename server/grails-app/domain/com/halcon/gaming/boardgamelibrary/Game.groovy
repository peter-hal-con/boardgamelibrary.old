package com.halcon.gaming.boardgamelibrary

class Game {
    String title
    Long bggId

    static constraints = {
        title nullable:false
        bggId nullable:true
    }
}
