# Huge files parsing
The microservice's purpose is to parse data from ccv files which have specific structure
## Minimum system requirements
Application needs two docker containers which use 1238 mb of disk space
so it needs about 6-8 of disk space for correct work and at least 8 GB of RAM

## How to run

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
 