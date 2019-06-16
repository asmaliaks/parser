package parser


import grails.rest.RestfulController
import org.springframework.web.multipart.MultipartFile
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ApiController extends RestfulController {
    static responseFormats = ['json', 'xml']
    ApiController() {
        super(Transaction)
    }

    def save(){
        MultipartFile file = request.getFile('file')
        File newFile = new File(file.part.location.path+'/file.csv')
        newFile.text = ""
        file.transferTo(newFile)
        String command = ""
        BigInteger line_no = 1
        Integer counter = 1
        Integer counterInserted = 1
        String line
        newFile.withReader { reader ->
            while ((line = reader.readLine())!=null) {
                if(counter == 1) {
                    command = "mysql -h localhost -u root -p5233 -e \"use parser; INSERT INTO municipality_transactions " +
                            "(company_id, " +
                            "year," +
                            " month, voucher_no, line_no, " +
                            "invoice_no, " +
                            "invoice_date, due_date, posting_date, rekontro_no, company_name, org_no, net_amount, vat, " +
                            "gross_amount, vat_code, receiver, poster, authoriser, assigner, type, responsible, service, object, " +
                            "project, text, version) VALUES "
                }
                List<String> item = line.split(";")
                String voucher_no = item[0] ?: "1"
                String invoice_no = item[3] ?: "1"
                Date invoice_date = Date.parse("yyyyMMdd", item[4])
                Date due_date = Date.parse("yyyyMMdd", item[5])
                Date posting_date = Date.parse("yyyyMMdd", item[6])
                Integer year  = item[7].substring(0, 4) as Integer
                Integer month = item[7].substring(4) as Integer
                String rekonto_no = item[8] ?: "1"
                String company_name = item[9] ?: "1"
                String org_no = item[10] ?: "1"
                BigDecimal net_amount = 10.00
                BigDecimal vat =  10.00
                BigDecimal gross_amount = 10.00
                String vat_code = item[14] ?: "1"
                String receiver = item[15] ?: "1"
                String poster = item[16] ?: "1"
                String authoriser = item[17] ?: "1"
                String assigner = item[18] ?: "1"
                String type = item[19] ?: "1"
                String responsible = item[20] ?: "1"
                String service = item[21] ?: "1"
                String object = item[22] ?: "1"
                String project = item[23] ?: "1"
                String text = item[28] ?: "1"
                if(line_no > 1){
                    command += ","
                }

                command += "(1, ${year}, ${month}, \'${voucher_no}\', \'${line_no}\', \'${invoice_no}\'," +
//                    "\'${invoice_date.getYear()}-${invoice_date.getMonth()}-${invoice_date.getDay()}\'," +
//                    "\'${due_date.getYear()}-${due_date.getMonth()}-${due_date.getDay()}\', " +
//                    "\'${posting_date.getYear()}-${posting_date.getMonth()}-${posting_date.getDay()}\', " +
                "\'2018-01-03\', \'2018-01-03\',\'2018-01-03\'," +
                "\'${rekonto_no}\', " +
                "\'${company_name}\',\'${org_no}\', ${net_amount}, " +
                "${vat}," +
                "${gross_amount}, \'${vat_code}\', \'${receiver}\', \'${poster}\', \'${authoriser}\', " +
                "\'${assigner}\'," +
                "\'${type}\', " +
                "\'${responsible}\', \'${service}\', \'${object}\', \'${project}\', \'${text}\',1)"
                line_no++
                counter++
                if(counter == 10){
                    counter = 1
                    println "command: ${command}"
                    def process = ['bash', '-c', command + "\""].execute()
                    process.waitFor()
                    command = ""
                    counterInserted++
                    println "inserted ${counterInserted}"
                }
            }
            def process = ['bash', '-c', command + "\""].execute()
            process.waitFor()
            println "lastpeace inserted"
        }
        respond([200])
    }
}
