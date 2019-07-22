#!/usr/bin/env bash

git pull --rebase ;
./gradlew clean war;
sudo rm -r /var/lib/tomcat8/webapps/app.war /var/lib/tomcat8/webapps/app  ;
sudo mv build/libs/parser-0.3-SNAPSHOT.war /var/lib/tomcat8/webapps/app.war;
sudo service tomcat8 restart