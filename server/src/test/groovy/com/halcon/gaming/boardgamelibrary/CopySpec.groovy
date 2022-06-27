package com.halcon.gaming.boardgamelibrary

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class CopySpec extends Specification implements DomainUnitTest<Copy> {

    def setup() {
    }

    def cleanup() {
    }

    User testUser = new User()

    void "test a Copy may not have a null uuid"() {
        expect:
        false == new Copy(uuid:null, owner:testUser).validate()
    }

    void "test a Copy may have a valid uuid"() {
        expect:
        true == new Copy(uuid:"a49150b4-087d-4eb3-895a-2bb351351637", owner:testUser).validate()
    }

    void "test a Copy may not have an invalid uuid"() {
        expect:
        false == new Copy(uuid:"not a uuid", owner:testUser).validate()
    }

    void "test a Copy with no uuid will be given one at construction time"() {
        expect:
        null != new Copy(owner:testUser).uuid
    }

    void "test a Copy that is given a uuid will keep it"() {
        expect:
        "a49150b4-087d-4eb3-895a-2bb351351637" == new Copy(uuid:"a49150b4-087d-4eb3-895a-2bb351351637", owner:testUser).uuid
    }

    void "test a Copy may not have a null owner"() {
        expect:
        false == new Copy(owner:null).validate()
    }
}
