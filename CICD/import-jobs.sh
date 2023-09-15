#!/bin/bash

echo "Starting importing jenkins jobs and credentials for Curiosity!"

echo Running in $SHELL

#echo Importing jenkins credentials for jobs:
#java -jar /var/jenkins_home/jenkins-cli.jar -s http://0.0.0.0:8080 -auth admin:123 import-credentials-as-xml "system::system::jenkins" < /var/jenkins_home/jenkins_credentials.xml

echo Importing job configurecuriosity:
java -jar /var/jenkins_home/jenkins-cli.jar -s http://0.0.0.0:8080 -auth admin:123 create-job configurecuriosity < /var/jenkins_home/configurecuriosity.xml
echo Importing job buildcuriosity:
java -jar /var/jenkins_home/jenkins-cli.jar -s http://0.0.0.0:8080 -auth admin:123 create-job buildcuriosity < /var/jenkins_home/buildcuriosity.xml
echo Importing job deploycuriosity:
java -jar /var/jenkins_home/jenkins-cli.jar -s http://0.0.0.0:8080 -auth admin:123 create-job deploycuriosity < /var/jenkins_home/deploycuriosity.xml
