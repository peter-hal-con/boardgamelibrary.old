package com.halcon.gaming.boardgamelibrary

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class TestOnlyControllerSpec extends Specification implements ControllerUnitTest<TestOnlyController> {

    def setup() {
    }

    def cleanup() {
    }

    void "test index"() {
        when:
        controller.index()

        then:
        status == 200
    }
}