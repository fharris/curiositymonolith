#!/bin/bash

echo "Starting configuration of Curiosity!"

echo Running in $SHELL

1.	Get the code from Github
If you haven’t done yet, get the code from the repository:

git clone https://github.com/fharris/curiositymonolith

2.	Create the curiositymonolith namespace

kubectl apply -f curiositymonolith-namespace.yaml


3.	Deploy database

kubectl apply -f ./databaseconfig/mysql-db-secret.yaml

You will notice that the password in the mysql-db-secret.yaml is set to the base64 of mySQLpword# 

kubectl apply -f ./databaseconfig/mysql-persistentvolume.yaml
kubectl apply -f ./databaseconfig/mysql-persistentvolumeclaim.yaml
kubectl apply -f ./databaseconfig/mysql-persistent-deploy.yaml
kubectl apply -f ./databaseconfig/mysql-db-service.yaml


kubectl -n curiositymonolith \ 
create secret generic curiositymonolith-mysql-db-secret \ 
--from-literal=SPRING_DATASOURCE_PASSWORD=$MYSQL_CREDENTIALS_PSW \
--from-literal=SPRING_DATASOURCE_USERNAME=$MYSQL_CREDENTIALS_USR

Replace $MYSQL_CREDENTIALS_PSW with the password like Welcome#1 and $MYSQL_CREDENTIALS_USR with the user which should be “curiosity”.

Configure the mysql database for the application  

When the mysql pod is running:

kubectl -n curiositymonolith port-forward svc/mysql-db-deployment 3306:80

And in a different tab, go to where you’ve clone the repository, and run the following command to create the user curiosity and the database curiositydb:

(You need to have a mysql client installed, change the password accordingly to what you used in step 333.,m)

mysql -h 127.0.0.1 -u root -pmySQLpword#2023 < ./ databaseconfig/create-curiositydb-resources.sql

If you want to validate, run :

mysql -h 127.0.0.1 -u root -pmySQLpword#2023;
And once logged, run:

SHOW DATABASES;

You should be able to see the curiositydb created. 


4.	Deploy the application

kubectl apply -f curiositymonolith-service-loadbalancer.yaml
kubectl apply -f ./appconfig/curiositymonolith-configmap.yaml
kubectl describe configmap curiositymonolith-configmap -n curiositymonolith
kubectl apply -f  ./appconfig/curiositymonolith-deployment.yaml -n curiositymonolith
#kubectl rollout restart -f curiositymonolith-deployment.yaml
kubectl get deployments -n curiositymonolith
kubectl get pods -n curiositymonolith

You should be able to see the curiositymonolith and respective database pods running. Lets access it with:

kubectl -n curiositymonolith port-forward svc/curiositymonolith-service-lb 9000:80

#kubectl -n curiositymonolith create secret generic curiositymonolith-mysql-db-secret --from-literal=SPRING_DATASOURCE_PASSWORD=Welcome#1 --from-literal=SPRING_DATASOURCE_USERNAME=curiosity

kubectl apply -f curiositymonolith-namespace.yaml
kubectl apply -f kubernetes-admin-tasks/jenkins-task-sa.yaml
kubectl apply -f kubernetes-admin-tasks/jenkins-task-sa-secret.yaml
kubectl get secrets jenkins-task-sa-secret -o json | jq -Mr '.data["token"]' | base64 -D
kubectl apply -f kubernetes-admin-tasks/jenkins-clusterrole.yaml
kubectl apply -f kubernetes-admin-tasks/jenkins-clusterrole-binding.yml
kubectl apply -f kubernetes-admin-tasks/jenkins-role.yaml
kubectl apply -f kubernetes-admin-tasks/jenkins-role-binding.yaml
