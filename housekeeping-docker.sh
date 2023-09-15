#!/bin/bash

echo "Cleaning everything"

echo Running in $SHELL


docker stop registry mysql gogs jenkins docker-dind; 
docker rm registry mysql gogs jenkins docker-dind; 
docker volume rm registry-data jenkins-data gogs-data mysql-data;

docker network rm cloudnative;


