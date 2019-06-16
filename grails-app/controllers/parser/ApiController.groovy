package parser

import grails.rest.RestfulController
import org.springframework.web.multipart.MultipartFile

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
        BigInteger line_no = 1
        File csvFile = new File(newFile.absolutePath.replace(newFile.name, 'fileCsv.csv'))
        csvFile.text = ""
        String line
        Integer counter = 0
        String append = ""
        newFile.withReader { reader ->
            while ((line = reader.readLine())!=null) {

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
                append += "${line_no},1,${vat_code},${rekonto_no},${object},${receiver},${responsible},1," +
                        "${month},2018-01-03 00:00:00,${net_amount},${poster},${authoriser},${text},${org_no},${service}," +
                        "${gross_amount}," +
                        "${company_name},${line_no},${type},2018-01-03 00:00:00,${invoice_no},${year},${project},${voucher_no},${assigner}," +
                        "2018-01-03 00:00:00,${vat}\n"
                if(counter == 500){
                    csvFile.append(append)
                    counter = 0
                    append = ""
                }
                line_no++
                counter++
            }
            csvFile.append(append)
            String command = "mysql -u root -p5233 -e \"use parser; LOAD DATA LOCAL INFILE '${csvFile.absolutePath}' INTO TABLE " +
                    "municipality_transactions FIELDS TERMINATED BY ','"
            def process = ['bash', '-c', command + "\""].execute()
            process.waitFor()
        }
        respond([200])
    }
}
