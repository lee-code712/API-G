def IMG = "192.168.100.12/commerce-yr/commerce-yr-gateway-img:v"

pipeline {

  environment {
    registry = "lee-code712/API-G"
    dockerImage = ""
  }

  agent any

  stages {

    stage('Checkout Source') {
      steps {
        echo "Checkout Source START"
        git 'https://github.com/lee-code712/API-G.git'
        echo "Checkout Source END"
      }
    }

    stage('Build image') {
      steps{
        script {
          echo "Build image START $BUILD_NUMBER"
          sh "docker build -t $IMG$BUILD_NUMBER ."
          echo "Build image END"
        }
      }
    }

    stage('Push Image') {
      environment {
               registryCredential = 'harbor'
           }
      steps{
        script {
          echo "Push Image START"
          sh "docker login 192.168.100.12 -u admin -p Unipoint11"
          sh "docker push $IMG$BUILD_NUMBER"
          }
        echo "Push Image END"
      }
    }
    

    stage('Deploy App') {
      steps {
        script {
          echo "Deploy App START"
          sh "/usr/local/bin/kubectl --kubeconfig=/home/jenkins/acloud-client.conf create -f gateway_deployment.yaml --image=$IMG$BUILD_NUMBER"
          echo "Deploy App END"
        }
      }
    }

  }

}
