#!/usr/bin/env groovy

pipeline {
  environment {
    registry = "fharris/curiosity"
    registry2 = "172.18.0.6:5000/curiosity"
    registryCredential = 'id-docker-registry'
    imageLatest = ''
    MYSQL_CREDENTIALS = credentials('id-mysql')
    MYSQL_HOST = credentials('id-mysql-host')
    mysql_network_host = '172.18.0.2'
    mysql_port = '3306'
    mysql_database_name = 'curiositydb'
    gogs_network_host = '172.18.0.3'
    gogs_port = '3000'
    
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

      stage('List source code') {
      steps {
        sh 'ls -ltra'
      }
    }
    
     stage('Building image') {
      steps{
        script {
          //imageLatest = docker.build(registry ,  "--build-arg SPRING_DATASOURCE_USERNAME=$MYSQL_CREDENTIALS_USR --build-arg SPRING_DATASOURCE_PASSWORD=$MYSQL_CREDENTIALS_PSW --build-arg SPRING_DATASOURCE_HOST=$mysql_network_host --build-arg SPRING_DATASOURCE_PORT=$mysql_port --build-arg SPRING_DATASOURCE_DBNAME=$mysql_database_name ." )
          imageLatest2 = docker.build(registry2 ,  "--build-arg SPRING_DATASOURCE_USERNAME=$MYSQL_CREDENTIALS_USR --build-arg SPRING_DATASOURCE_PASSWORD=$MYSQL_CREDENTIALS_PSW --build-arg SPRING_DATASOURCE_HOST=$mysql_network_host --build-arg SPRING_DATASOURCE_PORT=$mysql_port --build-arg SPRING_DATASOURCE_DBNAME=$mysql_database_name ." )
          //imageLatest = docker.build(registry ,  "--build-arg SPRING_DATASOURCE_USERNAME=$MYSQL_CREDENTIALS_USR --build-arg SPRING_DATASOURCE_PASSWORD='Welcome#1' --build-arg SPRING_DATASOURCE_HOST=$mysql_network_host --build-arg SPRING_DATASOURCE_PORT=$mysql_port --build-arg SPRING_DATASOURCE_DBNAME=$mysql_database_name ." )
        }
      }
    }
    /*
     stage('Pushing image to registry docker hub') {
      steps{
        script {
          docker.withRegistry( '', registryCredential ) {
            imageLatest.push()
          }
        }
      }
    }
    */
    stage('Pushing image to registry2 local') {
      steps{
        script {
          docker.withRegistry( '', registryCredential ) {
            imageLatest2.push()
          }
        }
      }
    }
  
  }
  post {
        // Clean after build
        always {
            cleanWs(cleanWhenNotBuilt: false,
                    deleteDirs: true,
                    disableDeferredWipeout: true,
                    notFailBuild: true,
                    patterns: [[pattern: '.gitignore', type: 'INCLUDE'],
                               [pattern: '.propsfile', type: 'EXCLUDE']])
        }
    }
}
