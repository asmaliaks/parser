package parser

import grails.core.GrailsApplication
import grails.rest.RestfulController
import org.springframework.web.multipart.MultipartFile

class ApiController extends RestfulController {
    static responseFormats = ['json', 'xml']
    GrailsApplication grailsApplication
    ApiController() {
        super(Transaction)
    }

    def save(){
        String sequrityToken = grailsApplication.config.getProperty("sequrity.token")
        if(request.getHeader("Authorization") != sequrityToken){
            log.debug "Authorization failed token:${sequrityToken}"
            response.status = 403
            respond(["Forbidden"])
            return
        }
        log.debug("START")
        MultipartFile file = request.getFile('file')
        InputStream inputStream = file.getInputStream()
        File newFile = new File('/tmp/file.csv')
        newFile.text = ""
        log.debug("START READING")
        BigInteger lineNumber = 1

        log.debug "Start reading file"
        FileWriter fileWriter = new FileWriter(newFile.absolutePath, true)
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)
        PrintWriter out = new PrintWriter(bufferedWriter)

        log.debug "Start reading input"
        inputStream.eachLine { String line ->
            out.print(convertLinIntoItemCommand(line, lineNumber))
            if(lineNumber % 1000 == 0){
                log.debug "Start reading line:${lineNumber}"
            }
            lineNumber++
        }
        log.debug "Finish reading input"

        log.debug "Start writing file"

        out.close()
        log.debug "Finish writing file"
        String dbUsername = grailsApplication.config.getProperty('dataSource.username')
        String dbPass = grailsApplication.config.getProperty('dataSource.password')
        String dbHostName = grailsApplication.config.getProperty('dataSource.dbHost')
        String dbEndpoint = grailsApplication.config.getProperty('dataSource.host')
        String dbName = grailsApplication.config.getProperty('dataSource.dbName')
        log.debug("data from file: ${newFile.absolutePath}")
        String cmd = "mysql --version"
        def pr = ['bash', '-c', cmd].execute()
        pr.waitFor()
        log.debug "mysql version: ${pr.text}"
        String createTable = "mysql -u ${dbUsername} -p${dbPass} -h ${dbEndpoint} -е \"use ${dbName}; CREATE TABLE " +
                "`municipality_transactions` (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT) ENGINE=InnoDB DEFAULT CHARSET=latin1;\" "
        def create = ['bash', '-c', createTable].execute()
        create.waitFor()
        log.debug "creating: ${create.text}"
        String command = "mysql -u ${dbUsername} -p${dbPass} -h ${dbEndpoint} " +
                " -e \"use ${dbName}; LOAD DATA LOCAL INFILE '${newFile.absolutePath}' INTO " +
                "TABLE " +
                "municipality_transactions FIELDS TERMINATED BY ','\""
        log.debug "${command}"
        def process = ['bash', '-c', command + "\""].execute()
        process.waitFor()
        log.debug "${process.text}"
        log.debug("FINISH READING")
        response.status = 200
        respond(["done"])
    }

    static String convertLinIntoItemCommand(final String line, final BigInteger lineNumber){
        List<String> item = line.split(";")
        String voucher_no = item[0] ?: ""
        String invoice_no = item[3] ?: ""
        String invoice_date = Date.parse("yyyyMMdd", item[4]).toTimestamp()
        String due_date = Date.parse("yyyyMMdd", item[5]).toTimestamp()
        String posting_date = Date.parse("yyyyMMdd", item[6]).toTimestamp()
        Integer year  = item[7].substring(0, 4) as Integer
        Integer month = item[7].substring(4) as Integer
        String rekonto_no = item[8] ?: ""
        String company_name = item[9] ?: ""
        String org_no = item[10] ?: ""
        BigDecimal net_amount = item[11] as Integer
        BigDecimal vat =  item[12] as Integer
        BigDecimal gross_amount = item[13] as Integer
        String vat_code = item[14] ?: ""
        String receiver = item[15] ?: ""
        String poster = item[16] ?: ""
        String authoriser = item[17] ?: ""
        String assigner = item[18] ?: ""
        String type = item[19] ?: ""
        String responsible = item[20] ?: ""
        String service = item[21] ?: ""
        String object = item[22] ?: ""
        String project = item[23] ?: ""
        String text = item[28] ?: ""

        return "${lineNumber},1,${vat_code},${rekonto_no},${object},${receiver},${responsible},1," +
                "${month},${invoice_date},${net_amount},${poster},${authoriser},${text},${org_no},${service}," +
                "${gross_amount}," +
                "${company_name},${lineNumber},${type},${due_date},${invoice_no},${year},${project}," +
                "${voucher_no},${assigner}," +
                "${posting_date},${vat}\n"
    }
}
