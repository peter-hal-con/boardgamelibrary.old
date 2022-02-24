package com.halcon.gaming.boardgamelibrary


import grails.rest.*
import grails.converters.*
import grails.gorm.transactions.Transactional

class TestOnlyController {
    static responseFormats = ['json', 'xml']

    def index() {
        render text:[] as JSON, contentType:'application/json'
    }

    @Transactional
    def createUser() {
        def data = request.JSON

        String username = data.username
        String password = data.password
        def user = new User(username:username, password:password)
        if (!user.save(flush:true)) {
            user.errors.allErrors.each {
                println it
            }
        }
        data.authorities.each {
            UserAuthority.create(user, Authority.findByAuthority(it), true)
        }

        render text:[id:user.id] as JSON, contentType:'application/json'
    }

    @Transactional
    def reset() {
        UserAuthority.findAll().each { it.delete(flush:true) }
        User.findAll().each { it.delete(flush:true) }
    }
}
