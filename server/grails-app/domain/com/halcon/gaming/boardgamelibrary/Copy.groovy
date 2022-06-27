package com.halcon.gaming.boardgamelibrary

class Copy {
    String uuid = UUID.randomUUID().toString()

    static constraints = {
        uuid nullable:false, validator: {
            try {
                return UUID.fromString(it).toString().equals(it)
            } catch (IllegalArgumentException e) {
                return false
            }
            return true
        }
    }
}
