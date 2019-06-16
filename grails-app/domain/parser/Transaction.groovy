package parser

import org.grails.datastore.gorm.GormEntity
import java.text.DecimalFormat

class Transaction implements GormEntity<Transaction> {

    Integer company_id
    Integer year
    Integer month
    String voucher_no
    String line_no
    String invoice_no
    Date invoice_date
    Date due_date
    Date posting_date
    String rekontro_no
    String company_name
    String org_no
    BigDecimal net_amount
    BigDecimal vat
    BigDecimal gross_amount
    String vat_code
    String receiver
    String poster
    String authoriser
    String assigner
    String type
    String responsible
    String service
    String object
    String project
    String text

    static mapping = {
        table "municipality_transactions"
    }

    static constraints = {
        company_id maxSize: 11, nullable: false
        year maxSize: 4, nullable: false
        month maxSize: 2, nullable: false
        voucher_no maxSize: 225, nullable: false
        line_no nullable: false, maxSize: 225
        invoice_no nullable: false, maxSize: 225
        invoice_date nullable:false
        due_date nullable: false
        posting_date nullable: false
        rekontro_no nullable: false, maxSize: 225
        company_name nullable: false, maxSize: 225
        org_no nullable: false, maxSize: 225
        net_amount nullable: false
        vat nullable: false
        gross_amount nullable: false
        vat_code nullable: false, maxSize: 225
        receiver nullable: false, maxSize: 225
        poster nullable: false, maxSize: 225
        authoriser nullable: false, maxSize: 225
        assigner nullable: false, maxSize: 225
        type nullable: false, maxSize: 225
        responsible nullable: false, maxSize: 225
        service nullable: false, maxSize: 225
        object nullable: false, maxSize: 225
        project nullable: false, maxSize: 225
        text nullable: false, maxSize: 225
    }
}
