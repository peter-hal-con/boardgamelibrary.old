package com.halcon.gaming.boardgamelibrary

import grails.gorm.transactions.Transactional
import grails.util.Environment

class BootStrap {
    @Transactional
    void addMissingAuthorities() {
        log.info "Loading database..."
        ["ROLE_ADMIN", "ROLE_COMMITTEE"].each { if(!Authority.findByAuthority(it)) new Authority(authority:it).save() }
    }

    @Transactional
    void addDevelopmentUsers() {
        User admin = new User(username:"admin@example.com", password:"password1").save()
        UserAuthority.create(admin, Authority.findByAuthority("ROLE_ADMIN"))

        User committee = new User(username:"committee@example.com", password:"password2").save()
        UserAuthority.create(committee, Authority.findByAuthority("ROLE_ADMIN"))

        User test = new User(username:"test@example.com", password:"password3").save()
    }

    def init = { servletContext ->
        addMissingAuthorities()

        if(Environment.current == Environment.DEVELOPMENT) {
            addDevelopmentUsers()
        }
    }
    def destroy = {
    }
}
