package com.halcon.gaming.boardgamelibrary


import grails.rest.*
import grails.converters.*

class TestOnlyController {
    static responseFormats = ['json', 'xml']

    def index() {
        render text:[] as JSON, contentType:'application/json'
    }
}
