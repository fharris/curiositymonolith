#!/bin/bash

echo "Starting configuration of Curiosity!"

echo Running in $SHELL

#kubectl -n curiositymonolith create secret generic curiositymonolith-mysql-db-secret --from-literal=SPRING_DATASOURCE_PASSWORD=Welcome#1 --from-literal=SPRING_DATASOURCE_USERNAME=curiosity

kubectl apply -f appconfig/curiositymonolith-namespace.yaml
kubectl apply -f jenkins-k8s-config/jenkins-task-sa.yaml
kubectl apply -f jenkins-k8s-config/jenkins-task-sa-secret.yaml
export JENKINS_TOKEN=`kubectl get secrets jenkins-task-sa-secret -o json | jq -Mr '.data["token"]' | base64 -d;`
echo "Save below token to past later in Jenkins"
echo $JENKINS_TOKEN
kubectl apply -f jenkins-k8s-config/jenkins-clusterrole.yaml
kubectl apply -f jenkins-k8s-config/jenkins-clusterrole-binding.yml
kubectl apply -f jenkins-k8s-config/jenkins-role.yaml
kubectl apply -f jenkins-k8s-config/jenkins-role-binding.yaml
