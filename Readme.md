# Huge files parsing
The microservice's purpose is to parse data from ccv files which have specific structure
## Minimum system requirements
Application needs two docker containers which use 1238 mb of disk space
so it needs about 6-8 of disk space for correct work and at least 8 GB of RAM
## HOW to run for AWS Ec2 Beanstalk 

1. Launch ubuntu 18.04 instance with public IP
configure security group, mine looks like the screenshot http://joxi.net/D2PEQQpSq8D6jA. (pay attention to ports)
2. create database using RDS  
connect to the instance via ssh and run following commands
         sudo apt-get update
        
         sudo apt install openjdk-8-jdk openjdk-8-jre
        
         sudo apt-get install tomcat8
        
         sudo apt-get install mysql-server -y
        
         cd
        
         git clone git@github.com:asmaliaks/parser.git
        
         cd parser

3. Then edit the file located grails-app/conf/application.yml https://prnt.sc/oij0mf
just put your parameters there
4. Then being in the root application directory run this comand:

        bash aids/deploy.sh
5. Once it’s done the application will be available by URL:
you_instance_ip:8080/app/

    CURL request example:
    
            curl -i -X POST \
               -H "Authorization:s5He7Yo35gIb9U195pB4r9" \
               -H "Content-Type:multipart/form-data" \
               -F "file=@\"./csv_small.csv\";type=text/csv;filename=\"csv_small.csv\"" \
             'http://you_instance_ip:8080/app/api/save'
