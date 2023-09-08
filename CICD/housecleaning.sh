#!/bin/bash

echo "Cleaning everything"

echo Running in $SHELL


docker stop registry mysql gogs jenkins jenkins-docker; 
docker rm registry mysql gogs jenkins jenkins-docker; 
docker volume rm registry-data jenkins-data gogs-data mysql-data;

docker network rm jenkins;


