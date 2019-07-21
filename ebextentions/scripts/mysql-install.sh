#!/bin/bash

cd ~/
wget http://repo.mysql.com/mysql-apt-config_0.8.9-1_all.deb
dpkg -i mysql-apt-config_0.8.9-1_all.deb
apt-get update
apt-get install mysql-server
mysql_secure_installation

