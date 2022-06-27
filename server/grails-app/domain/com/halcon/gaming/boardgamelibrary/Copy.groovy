package com.halcon.gaming.boardgamelibrary

class Copy {
    String uuid = UUID.randomUUID().toString()
    User owner
    Game game

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
        game nullable:false
    }
}
