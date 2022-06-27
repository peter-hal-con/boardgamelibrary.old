package com.halcon.gaming.boardgamelibrary

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class GameSpec extends Specification implements DomainUnitTest<Game> {

    def setup() {
    }

    def cleanup() {
    }

    void "test a Game may not have a null title"() {
        expect:
        false == new Game(title:null).validate()
    }

    void "test a Game may have a title"() {
        expect:
        true == new Game(title:"Some Title").validate()
    }

    void "test a Game may have a null bggId"() {
        expect:
        true == new Game(title:"Some Title", bggId:null).validate()
    }
}
