#!/bin/bash

echo "Starting local dev configuration"

echo Running in $SHELL

#echo Pull containers or build in the case of Jenkins and Docker Dind
#docker pull docker:dind
#docker pull fharris/jenkins:jcasc
#docker pull mysql:5.7
#docker pull gogs/gogs

echo Starting Docker Network
docker network create --subnet=172.18.0.0/16 jenkins;

echo Starting Docker Registry:2 Container
docker run -d -p 5000:5000 --restart=always --name registry -v /mnt/registry:/var/lib/registry registry:2

echo Starting MySQL Container
docker run --name mysql --restart=on-failure --detach --network jenkins --ip 172.18.0.2  --env MYSQL_ROOT_PASSWORD=mySQLpword#2023 --volume mysql-data:/var/lib/mysql --publish 9306:3306 mysql:5.7

mysql -h 127.0.0.1 --port 9306 -u root -pmySQLpword#2023 < create-curiositydb-resources.sql

echo Starting Gogs Container
docker run --name gogs --restart=on-failure --detach --network jenkins --ip 172.18.0.3  --publish 10022:22 --publish 10880:3000 --volume gogs-data:/data gogs/gogs

echo Configuring Gogs...
#docker exec -it gogs sh -c \ "sleep 10; echo 'LOCAL_NETWORK_ALLOWLIST = *' >> /data/gogs/conf/app.ini"

echo Starting Jenkins-Docker Container
docker run --name jenkins-docker --restart on-failure --detach --privileged --network jenkins --ip 172.18.0.4 --network-alias docker  --env DOCKER_TLS_CERTDIR=/certs   --volume jenkins-docker-certs:/certs/client --volume jenkins-data:/var/jenkins_home  -p 2376:2376  docker:dind --storage-driver overlay2

echo Starting Jenkins Container
docker run --name jenkins --restart on-failure --detach  -p 8080:8080 -p 50000:50000 --env JENKINS_ADMIN_ID=admin --env JENKINS_ADMIN_PASSWORD=123 --env DOCKER_HOST=tcp://docker:2376 --env DOCKER_CERT_PATH=/certs/client --env DOCKER_TLS_VERIFY=1 --volume jenkins-docker-certs:/certs/client:ro --volume jenkins-data:/var/jenkins_home --network jenkins --ip 172.18.0.5 fharris/jenkins:jcasc

echo Configuring Jenkins...
echo "Running configuration script inside jenkins...be patiente...around 30 seconds!"
docker exec -it jenkins sh -c 'cd $HOME;sleep 60;./import-jobs.sh'

echo Done
