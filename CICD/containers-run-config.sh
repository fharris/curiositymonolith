#!/bin/bash

echo "Starting local dev configuration"

echo "ATTENTION FERNANDO: -->>> ***** STILL NEED TO LIMIT THE RESOURCES USED BY CONTAINERS!!!"
echo "ATTENTION FERNANDO: -->>> ***** STILL NEED TO put host or IPs as variables across the script!!!"

echo Running in $SHELL

#echo Pull containers or build in the case of Jenkins and Docker Dind
#docker pull docker:dind
#docker pull fharris/jenkins:jcasc
#docker pull mysql:5.7
#docker pull gogs/gogs


echo "+------------------------------------+"
echo "| Starting Network                   |"
echo "+------------------------------------+"


echo Starting Docker Network
docker network create --subnet=172.18.0.0/16 cloudnative;

echo "+------------------------------------+"
echo "| Starting Containers                |"
echo "+------------------------------------+"


echo Starting Jenkins Container...
echo "If you notice Jenkins is running too slow, try to increase the memory allocated to the container in the command below,"
echo "or the memory allocated to the Box where your Docker is running"
docker run --name jenkins --restart on-failure --detach  -p 8080:8080 -p 50000:50000 --env JAVA_OPTS="-Xmx2048m -Djava.awt.headless=true" --env JENKINS_ADMIN_ID=admin --env JENKINS_ADMIN_PASSWORD=123 --env DOCKER_HOST=tcp://docker:2376 --env DOCKER_CERT_PATH=/certs/client --env DOCKER_TLS_VERIFY=1 --volume jenkins-docker-certs:/certs/client:ro --volume jenkins-data:/var/jenkins_home --network cloudnative --ip 172.18.0.5 fharris/mynewjenkins:test


echo Starting Docker Registry:2 Container
docker run -d -p 5000:5000 --restart=on-failure --detach --network cloudnative --ip 172.18.0.6 --name registry --volume registry-data:/var/lib/registry registry:2

echo Starting MySQL Container
docker run --name mysql --restart=on-failure --detach --network cloudnative --ip 172.18.0.2  --env MYSQL_ROOT_PASSWORD=mySQLpword#2023 --volume mysql-data:/var/lib/mysql --publish 9306:3306 mysql:8

#sleep 30;
# copy the files and docker exec instead of running mysql --> https://stackoverflow.com/questions/22907231/how-to-copy-files-from-host-to-docker-container
#mysql -h 127.0.0.1 --port 9306 -u root -pmySQLpword#2023 < ./databaseconfig/create-curiositydb-resources.sql
#mysql -h 127.0.0.1 --port 9306 -u curiosity -pWelcome#1 -e 'SHOW DATABASES;'

echo Starting Gogs Container
docker run --name gogs --restart=on-failure --detach --network cloudnative --ip 172.18.0.3  --publish 10022:22 --publish 10880:3000 --volume gogs-data:/data gogs/gogs


echo Starting Docker-Dind
docker run --name docker-dind --restart on-failure --detach --privileged --network cloudnative --ip 172.18.0.4 --network-alias docker  --env DOCKER_TLS_CERTDIR=/certs   --volume jenkins-docker-certs:/certs/client --volume jenkins-data:/var/jenkins_home  -p 2376:2376  docker:dind --storage-driver overlay2

echo "+-------------------------------------+"
echo "| Configuring Containers              |"
echo "|                                     |"
echo "|                                     |"
echo "|                                     |"
echo "+-------------------------------------+"

echo Configuring Gogs...
# maybe having app.ini with all ready!!
#or implementing a wait to press button and ask user to open browser and configure before continuing

echo "+---------------------------------------------------------------------------+"
echo "|    **ATENTION**                                                           |"
echo "| Open your browser in localhost:10880 and configure Gogs before continuing.|"
echo "| Follow the steps explained in Github and press the blue button to install|"
echo "| When you're done return to this script and press *ENTER* to continue      |"
echo "|                                                                           |"
echo "+---------------------------------------------------------------------------+"
read -p ""

docker exec -it gogs sh -c \ "echo 'LOCAL_NETWORK_ALLOWLIST = *' >> /data/gogs/conf/app.ini; chmod 777 /data/gogs/conf/app.ini; "
docker exec -it gogs sh -c \ "cat /data/gogs/conf/app.ini;"
docker restart gogs

echo Configuring Docker Dind...
docker exec -it docker-dind sh -c 'mkdir /etc/docker;'
docker cp CICD/daemon.json docker-dind:/etc/docker   
docker restart docker-dind
sleep 15;
docker exec -it docker-dind sh -c 'docker info'


echo Configuring Jenkins...
echo "Running configuration script inside jenkins...be patient...wait a couple of minutes!"
docker exec -it jenkins sh -c 'cd $HOME;sleep 30;./import-jobs.sh'


echo Configuring MySQL...
#MySQL container should be up...time to configure database
# copy the files and docker exec instead of running mysql --> https://stackoverflow.com/questions/22907231/how-to-copy-files-from-host-to-docker-container
mysql -h 127.0.0.1 --port 9306 -u root -pmySQLpword#2023 < ./databaseconfig/create-curiositydb-resources.sql
mysql -h 127.0.0.1 --port 9306 -u curiosity -pWelcome#1 -e 'SHOW DATABASES;'

echo Done
