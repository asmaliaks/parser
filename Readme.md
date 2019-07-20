# Huge files parsing
The microservice's purpose is to parse data from ccv files which have specific structure
## Minimum system requirements
Application needs two docker containers which use 1238 mb of disk space
so it needs about 6-8 of disk space for correct work and at least 8 GB of RAM
## HOW to run for AWS Ec2 Beanstalk 

1.If there is a wish to create .war file by own hands (CAN BE SKIPPED. if you want to skip this then go to the 2-nd 
step)
- Install jdk 8 

for CentOS:
####### Remove java 7 if existed
> sudo yum remove -y java

####### Install basic packages
> sudo yum install -y git

####### Download and install java 8
> wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; 
oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/8u131-b11/d54c1d3a095b4ff2b6607d096fa80163/jdk-8u131-linux-x64.tar.gz"
tar -xzvf jdk-8u131-linux-x64.tar.gz
rm -rf jdk-8u131-linux-x64.tar.gz

####### Configure JAVA_HOME
>sudo vim ~/.bashrc

add following strings
>alias cls='clear'
>export JAVA_HOME=~/jdk1.8.0_131
>export JRE_HOME=~/jdk1.8.0_131/jre
>export PATH=$PATH:~/jdk1.8.0_131/bin:/~/jdk1.8.0_131/jre/bin

then run these 

>source ~/.bashrc 

>java -version

if you see smth like this that means you did great
>openjdk version "1.8.0_212"
 OpenJDK Runtime Environment (build 1.8.0_212-8u212-b03-0ubuntu1.16.04.1-b03)
 OpenJDK 64-Bit Server VM (build 25.212-b03, mixed mode)

If it needs to be installed on Ubuntu it can be easily googled. commands can be different from one to another version
 of Ubuntu
  
  - SDKMAN installation for CentOS
  > yum install which
  
  > yum install unzip
  
  > yum install zip
  
  > cd /opt/
  
  > curl -s "https://get.sdkman.io" | bash
  
  > source "$HOME/.sdkman/bin/sdkman-init.sh"
  
  > sdk version
  
  (if sdk version is seen that means everything is OK)
  
  for Ubuntu:
  > curl -s https://get.sdkman.io | bash
  
  > source "$HOME/.sdkman/bin/sdkman-init.sh"
  
  - Grails installation
  > sdk install grails 3.2.11
  
  > sdk use grails 3.2.11
  
  > grails -version
  
  If you see following, that means it went good:
  >| Grails Version: 3.3.10
  
  >| JVM Version: 1.8.0_212
  
  - Mysql installation
  
  version of mysql has to be installed not higher than 5.7.26 (can be freely googled, command can be differet from 
  version to version of OS).  
  Database 'parser' needs to be created after msyql installation
  
  - Cloning project from git repository
  > git clone git@github.com:asmaliaks/parser.git
  
  Then find confg file and set DB credentials:
  
  > parser/grails-app/conf/application.yml
  
  and changes `username` (`environments.development.dataSource.username`) and `password` (`environments.development
  .dataSource.password`).
  - Generating .war file
  Being in the root folder of the app run command: 
  > grails war
  
  Now we have .war file which located in `build/libs` and has .war extension
  
  If previous things were done well 
  
  2.Creating Beanstalk environment specially for grails in aws console :
- http://joxi.ru/V2VpKKVidDn6Qr

- http://joxi.ru/xAeE00aSRqyQNA

- http://joxi.ru/brRj66bH7EbKJr or http://joxi.ru/nAyQ88jSgen7QA and select "Create environment"

- http://joxi.ru/Dr81EELio5bwpm

- http://joxi.ru/KAxX99EtZjoyjA

- http://joxi.ru/12M4jj0HlQJZPm

    Click "Create environment" when uploading complete
    
- http://joxi.ru/L21lbbYTRxQGgm (wait a few minutes)

- http://joxi.ru/BA0OvvDTMXQl92  it meаns OK

- http://joxi.ru/J2byllLh0WDRGm

- http://joxi.ru/xAeE00aSRqOn7A
endpoint for database RDS with opened 3360 port
http://joxi.ru/V2VpKKVidDQbRr
and add DB_USERNAME and DB_PASS
  





## How to run (ONLY FOR DOCKER)

1.build docker container using following command

> ```sudo docker build -t parser .```
 
 2.Once the container has been created it needs to be started
 
> `sudo docker run -p 80:8080 -p 3306:3306 -ti parser`

If following string is seen that means the application is running:
> `Grails application running at http://localhost:8080 in environment: development`
 
 #HOW TO WORK WITH
 
 If everything went properly we can use the service. Now it's running at `http://localhost` and works as restful API
 There is one single endpoint to work with files.
 endpoint is `/api/save` and complete url looks like `http://localhost/api/save`
 Can be reached with CURL or other tools which can send POST request to the endpoint.
 ## Required to know
 POST request must contain following headers:
 - `Content-Type: multipart/form-data`
 - `Authorization: s5He7Yo35gIb9U195pB4r9`
 Header Authorization contains a token which can be changed in the file located `grails-app/conf/application.yml` 
 relatively to root folder of the project, the parameter called `token` and it's located in sequrity section
 - request method is POST
 - file name of the input `file`
 and whole CURL command should have following look:
 > ``curl -i -X POST \
      -H "Content-Type:multipart/form-data" \
      -H "Authorization:s5He7Yo35gIb9U195pB4r9" \
      -F "file=@\"./csv.csv\";type=text/csv;filename=\"csv.csv\"" \
    'http://localhost/api/save'``
    
    
 If the file has proper mapping it's data will be parsed and saved in the table `municipality_transactions` of the 
 database
 
 ##How to check parsed data in the database
 When .csv file has been successfully parsed the parsed data cah be checked with following steps:
 1. Run following command to get running containers 
 
    > ``docker ps -l``
    
    if everything is ok at least one container should be in the command's output. For example:
    
     > ``CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              
     PORTS                                                     NAMES``
      
     > ``09df785d76a7        mytest              "/app/start_mysql...."   14 minutes ago      Up 13 minutes       0.0
        .0.0:3306->3306/tcp, 33060/tcp, 0.0.0.0:80->8080/tcp   blissful_noyce``
 
    we need `CONTAINER ID` in this case it looks like this `09df785d76a7`
 2. Now to make bash commands being in container run this command:
    > ``docker exec -it hash_from_previous_step /bin/bash``
 
    it should lead into the container and we should see smth like this:
    > ``root@09df785d76a7:/app#``
 3. Run command to connect to container's mysql:
    > ``mysql -u root -p1234``    
    
    Now console should show `mysql>`
    
 4. Then being in mysql console run following commands:    
    > `use parser;`
    
    > `select * from municipality_transactions;`
    
    then console should output all the entries parsed and saved in the database
 