#!/bin/bash

echo "Starting configuration of Jenkins Account for Curiosity!"

echo Running in $SHELL

#kubectl -n curiositymonolith create secret generic curiositymonolith-mysql-db-secret --from-literal=SPRING_DATASOURCE_PASSWORD=Welcome#1 --from-literal=SPRING_DATASOURCE_USERNAME=curiosity

kubectl apply -f kubernetesconfig/curiositymonolith-namespace.yaml
kubectl apply -f kubernetes-admin-tasks/jenkins-task-sa.yaml
kubectl apply -f kubernetes-admin-tasks/jenkins-task-sa-secret.yaml
kubectl get secrets jenkins-task-sa-secret -o json | jq -Mr '.data["token"]' | base64 -D
kubectl apply -f kubernetes-admin-tasks/jenkins-clusterrole.yaml
kubectl apply -f kubernetes-admin-tasks/jenkins-clusterrole-binding.yml
kubectl apply -f kubernetes-admin-tasks/jenkins-role.yaml
kubectl apply -f kubernetes-admin-tasks/jenkins-role-binding.yaml