# Huge files parsing
The microservice's purpose is to parse data from ccv files which have specific structure
## Minimum system requirements
Application needs two docker containers which use 1238 mb of disk space
so it needs about 6-8 of disk space for correct work and at least 8 GB of RAM

## How to run

1.create mysql container using following command

>```sudo docker run --name parser-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=db_pass -d mysql:5.7.26```
 
 where `db_pass` is your database password and `parser-mysql` is the container's name both can be random
 
 2.Once the container has been created database needs to be created
 - enter into container
 
> `docker exec -it parser-mysql /bin/bash`

> `mysql -u root -pdb_pass`
 
 - where `db_pass` is the database's password
 
 > `create schema parser;`
 
 > `exit;`
 
 where `parser` is the database 
 
 3.Then IP address of `parser-mysql` must be found
 > `docker inspect parser-mysql`
 
 There will be `IPAddress` parameter in the input
 
 Copy the IP address and paste it into file located `grails-app/conf/application.yml`
 as parameter of `environments.development.dataSource.url`
 
 it should look like this `jdbc:mysql://your_IP_address/parser`
 
 where `your_IP_addres` is the IP address and `parser` is a name of the database
 
 4.Then go to the project's root folder
 > `cd /some/folder/parser
 
 - then build image with application using following command
 > `sudo docker build -t parser .` (don't miss the dot)
 
 - and wait until execution goes through all steps and last string be displayed
 
 >   ```Successfully tagged parser:latest```
 
 5.If everything is successful let's run the container where application works in
 > `sudo docker run -p 80:8080 parser`
 
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
 