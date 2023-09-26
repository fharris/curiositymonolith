#!/usr/bin/env groovy

pipeline {
  environment {
    //registry = "fharris/curiosity"
    //registryCredential = 'id-docker-registry'
    dockerImageBuild = ''
    dockerImageLatest = ''
    MYSQL_CREDENTIALS = credentials('id-mysql')
    //kubernetes_proxy = 'https://192.168.5.15:6443'
    kubernetes_proxy = "${env.KUBERNETES_ENDPOINT}"
  }
  agent any
  stages{
      
      
      
    stage('Get source code') {
      steps {
        git branch: 'main', 
            url: 'http://gogs:3000/gogs-user/curiositymonolith.git'
      }
    }
    
    
    stage('Checkout code') {
        steps {
            checkout scm
        }
    }
   
    
      stage('Listing source code') {
      steps {
        sh 'ls -ltra'
      }
    }
      
      stage('Configurion in Kubernetes') {
      steps {
        withKubeConfig( credentialsId: 'jenkins-token-kubernetes', serverUrl: kubernetes_proxy ) {
            sh "kubectl apply -f kubernetesconfig/curiositymonolith-namespace.yaml"
            sh "kubectl apply -f databaseconfig/mysql-db-secret.yaml"
            sh "kubectl apply -f databaseconfig/mysql-persistent-deploy.yaml"
            sh "kubectl apply -f databaseconfig/mysql-persistentvolume.yaml"
            sh "kubectl apply -f databaseconfig/mysql-persistentvolumeclaim.yaml"
            sh "kubectl apply -f databaseconfig/mysql-persistent-deploy.yaml"
            sh "kubectl apply -f databaseconfig/mysql-db-service.yaml"
            sh "kubectl apply -f kubernetesconfig/curiositymonolith-service-loadbalancer.yaml"
            sh "kubectl -n curiositymonolith get --no-headers=true pods -l app=mysql-db -o custom-columns=:metadata.name"
            sh "sleep 15"
            sh "kubectl -n curiositymonolith exec -it `kubectl -n curiositymonolith get --no-headers=true pods -l app=mysql-db -o custom-columns=:metadata.name` -- mysql -h 127.0.0.1 -u root -pmySQLpword#2023 < ./databaseconfig/create-curiositydb-resources.sql"
            sh 'kubectl -n curiositymonolith create secret generic curiositymonolith-mysql-db-secret --from-literal=SPRING_DATASOURCE_PASSWORD=$MYSQL_CREDENTIALS_PSW --from-literal=SPRING_DATASOURCE_USERNAME=$MYSQL_CREDENTIALS_USR --dry-run=client -o yaml > curiositymonolith-mysql-db-secret.yaml'
            sh "kubectl apply -f curiositymonolith-mysql-db-secret.yaml"
        }
      }
    }

  
  }
  post {
        // Clean after build
        always {
            cleanWs(cleanWhenNotBuilt: true,
                    deleteDirs: true,
                    disableDeferredWipeout: false,
                    notFailBuild: false,
                    patterns: [[pattern: '.gitignore', type: 'INCLUDE'],
                               [pattern: '.propsfile', type: 'EXCLUDE']])
        }
    }
}