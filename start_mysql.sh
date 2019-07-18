#!/bin/bash

#sh /usr/local/bin/docker-entrypoint.sh
bash /entrypoint.sh

#restart or start mysql service
service mysql restart

#Grans access to root user from all hosts ( remote, external and localhost)
mysql -uroot -p${MYSQL_ROOT_PASSWORD} -e "GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY '${MYSQL_ROOT_PASSWORD}' WITH GRANT OPTION ;"
mysql -uroot -p${MYSQL_ROOT_PASSWORD} -e "FLUSH PRIVILEGES ;";

#show initial mysql databases inside container ( tthe first run will not show 'parser' db)
mysqlshow 
mysql -uroot -p${MYSQL_ROOT_PASSWORD} -e "CREATE SCHEMA IF NOT EXISTS ${MYSQL_DATABASE} ;"
#show mysql databses after 'parser' db creation
mysqlshow 

#print IP settins 
ifconfig -a

#restart mysql server with granted permissions
service mysql restart

#run grails app
grails run-app


echo "Press [CTRL+C] to stop.. container"
while true
do    
    sleep 1
done