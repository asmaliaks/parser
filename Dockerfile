
#----------------------------------------------------------------------------------

# step 1 create docker image
# sudo docker build -t parser .

# step 2 run docker image with port 80 for http and 3306 for connection  with mysql client
# sudo docker run -p 80:8080 -p 3306:3306 -ti parser

#----------------------------------------------------------------------------------



FROM mysql:5.7.26


# set database name and root user password
ENV MYSQL_HOST localhost
ENV MYSQL_ROOT_PASSWORD 1234
ENV MYSQL_DATABASE parser

ENV GOSU_VERSION 1.7

# install Java
RUN echo 'Install JAVA' && \   
    mkdir -p /usr/share/man/man1 && \
    apt-get update -y && \
    apt-get install default-jdk -y

# test java 
RUN java -version

# install some tools which allows to install grails and help work inside container 
RUN echo 'Install missing tools' && \
    apt-get install wget unzip net-tools nano mc -y

# describe versions
ENV GRAILS_VERSION=3.3.10

#Other variables
ENV GRAILS_HOME /usr/lib/jvm/grails

#Install Grails
WORKDIR /usr/lib/jvm
RUN set -o errexit -o nounset \
    && echo "Downloading Grails" \
    && wget --no-verbose --output-document=grails.zip "https://github.com/grails/grails-core/releases/download/v${GRAILS_VERSION}/grails-${GRAILS_VERSION}.zip" \
    \
    && echo "Instaling Grails" \
    && unzip grails.zip  \
    && rm grails.zip \
    && mv "grails-${GRAILS_VERSION}" "${GRAILS_HOME}/" \
    && ln --symbolic "${GRAILS_HOME}/bin/grails" /usr/bin/grails \
    \
    && echo "Testing Grails installation" \
    && grails --version

#Copy application files
WORKDIR /app
COPY . /app

# compile application
RUN grails clean
RUN grails compile

# start mysql service wne run container
COPY start_mysql.sh /app/start_mysql.sh
RUN chmod 777 /app/start_mysql.sh
RUN ln -s /app/start_mysql.sh /entrypoint_mysql.sh # backwards compat

#Open port
EXPOSE 8080 3306 33060

#Execute script when run container
ENTRYPOINT ["/app/start_mysql.sh"]

#Run command for docker image
CMD ["/bin/bash","run"]
