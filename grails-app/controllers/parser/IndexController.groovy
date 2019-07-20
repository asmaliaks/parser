package parser


import grails.rest.*
import grails.converters.*

class IndexController {
	static responseFormats = ['json', 'xml']
	
    def index() {
        response.status = 200
        respond(["Hello"])
    }
}
