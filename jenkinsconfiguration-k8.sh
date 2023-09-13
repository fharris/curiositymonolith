#!/bin/bash

echo "Starting configuration of Curiosity!"

echo Running in $SHELL

#kubectl -n curiositymonolith create secret generic curiositymonolith-mysql-db-secret --from-literal=SPRING_DATASOURCE_PASSWORD=Welcome#1 --from-literal=SPRING_DATASOURCE_USERNAME=curiosity

kubectl apply -f kubernetesconfig/curiositymonolith-namespace.yaml
kubectl apply -f kubernetesconfig/jenkins-task-sa.yaml
kubectl apply -f kubernetesconfig/jenkins-task-sa-secret.yaml
export JENKINS_TOKEN=`kubectl get secrets jenkins-task-sa-secret -o json | jq -Mr '.data["token"]' | base64 -D;`
echo $JENKINS_TOKEN
kubectl apply -f kubernetesconfig/jenkins-clusterrole.yaml
kubectl apply -f kubernetesconfig/jenkins-clusterrole-binding.yml
kubectl apply -f kubernetesconfig/jenkins-role.yaml
kubectl apply -f kubernetesconfig/jenkins-role-binding.yaml
