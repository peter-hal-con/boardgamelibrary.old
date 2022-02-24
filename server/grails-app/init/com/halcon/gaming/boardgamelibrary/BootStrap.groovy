package com.halcon.gaming.boardgamelibrary

import grails.gorm.transactions.Transactional

class BootStrap {
    @Transactional
    void addMissingAuthorities() {
        log.info "Loading database..."
        ["ROLE_ADMIN", "ROLE_COMMITTEE"].each { if(!Authority.findByAuthority(it)) new Authority(authority:it).save() }
    }

    def init = { servletContext ->
        addMissingAuthorities()
    }
    def destroy = {
    }
}
