def IMG = "192.168.100.12/commerce-yr/commerce-yr-gateway-img"
def KC = "/usr/local/bin/kubectl --kubeconfig=/home/jenkins/acloud-client.conf"

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
          sh "docker build --no-cache -t ${IMG}:gateway-${BUILD_NUMBER} ."
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
          sh "docker push ${IMG}:gateway-${BUILD_NUMBER}"
          }
        echo "Push Image END"
      }
    }
    

    stage('Deploy App') {
      steps {
        script {
          echo "Deploy App START"
          sh "${KC} apply -f gateway_deployment.yaml"
          sh "${KC} set image deployment/commerce-yr-gateway-v2 commerce-yr-gateway=${IMG}:gateway-${BUILD_NUMBER} -n commerce-yr"
          echo "Deploy App END"
        }
      }
    }

  }

}
