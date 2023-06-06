#!/bin/bash

echo "Starting configuration of Curiosity!"

echo Running in $SHELL

#kubectl -n curiositymonolith create secret generic curiositymonolith-mysql-db-secret --from-literal=SPRING_DATASOURCE_PASSWORD=Welcome#1 --from-literal=SPRING_DATASOURCE_USERNAME=curiosity
