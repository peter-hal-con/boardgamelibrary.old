package com.halcon.gaming.boardgamelibrary

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class TitleSpec extends Specification implements DomainUnitTest<Title> {

    def setup() {
    }

    def cleanup() {
    }

    void "test a Title may not have a null name"() {
        expect:
        false == new Title(name:null).validate()
    }

    void "test a Title may have a name"() {
        expect:
        true == new Title(name:"Some Title").validate()
    }

    void "test a Title may have a null bggId"() {
        expect:
        true == new Title(name:"Some Title", bggId:null).validate()
    }
}
