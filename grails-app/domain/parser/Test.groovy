package parser

import org.grails.datastore.gorm.GormEntity

class Test implements GormEntity<Test> {

    String company_name

    static mapping = {
        table "test"
    }

    static constraints = {
        company_name nullable: false, maxSize: 225
    }
}
