####################################
## docker instruction for console
####################################

#step 1
# add mysql server docker image
#sudo docker run --name parser-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=5233 -d mysql:5.7.26

#1.1 get container IP and put into config
# docker inspect parser-mysql // and find ip address in the input

#step 2
#build docker image with command
# sudo docker build -t parser .

#step 3
#run docker image with command
# sudo docker run -p 80:8080 parser


#Initial container OpenJDK 8
FROM openjdk:8

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


#Copy files
WORKDIR /app
COPY . /app

RUN gradle clean
RUN grails clean
RUN grails compile

#Open port
EXPOSE 8080

#Run command for docker image
CMD ["grails","run-app"]

