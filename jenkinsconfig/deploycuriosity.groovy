#!/usr/bin/env groovy

pipeline {
  environment {
    //registry = "fharris/curiosity"
    //registryCredential = 'id-docker-registry'
    //dockerImageBuild = ''
    //dockerImageLatest = ''
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
      stage('Checking source code') {
      steps {
        sh 'ls -ltra'
      }
    }
   
      stage('Deployment  to Kubernetes') {
      steps {
        withKubeConfig( credentialsId: 'jenkins-token-kubernetes', serverUrl: kubernetes_proxy ) {
            sh "echo 'KUBERNETES ENDPOINT=$KUBERNETES_ENDPOINT'"
            sh "kubectl apply -f appconfig/curiositymonolith-configmap.yaml"
            sh "kubectl describe configmap curiositymonolith-configmap -n curiositymonolith"
            sh "kubectl rollout restart -f appconfig/curiositymonolith-deployment.yaml"
            sh "kubectl get deployments -n curiositymonolith"
            sh "kubectl get pods -n curiositymonolith"
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
