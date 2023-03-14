pipeline {
  agent any

  parameters {
    booleanParam(name : 'BUILD_DOCKER_IMAGE', defaultValue : true, description : 'BUILD_DOCKER_IMAGE')
    booleanParam(name : 'RUN_TEST', defaultValue : true, description : 'RUN_TEST')
    booleanParam(name : 'PUSH_DOCKER_IMAGE', defaultValue : true, description : 'PUSH_DOCKER_IMAGE')

    // CI
    string(name : 'DOCKER_IMAGE_NAME', defaultValue : 'demo', description : 'DOCKER_IMAGE_NAME')
    string(name : 'DOCKER_TAG', defaultValue : '1.0.0', description : 'DOCKER_TAG')
  }

  environment {
    REPOSITORY = "howdi2000/edu-msa-board"
    DOCKERHUB_CREDENTIALS = credentials('docker-hub')
    DOCKER_IMAGE = "${REPOSITORY}/${params.DOCKER_IMAGE_NAME}"
    DOCKER_TAG = "${params.DOCKER_TAG}"
  }

  stages {
    stage('============ Build Docker Image ============') {
        when { expression { return params.BUILD_DOCKER_IMAGE } }
        agent { label 'build' }
        steps {
            dir("${env.WORKSPACE}") {
                sh '''
                    mvn package -f pom.xml
                    docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .
                '''
            }
        }
        post {
            always {
                echo "Docker build success!"
            }
        }
    }
    stage('============ Run test code ============') {
        when { expression { return params.RUN_TEST } }
        agent { label 'build' }
        steps {
            sh'''
                docker login --username howdi2000 --password dufrms@0418
                docker push ${DOCKER_IMAGE}:${DOCKER_TAG}
            '''
        }
    }
  }

  post {
    failure {
      slackSend(
        channel: "#jenkins_test",
        color: "danger",
        message: "[Failed] Job:${env.JOB_NAME}, Build num:#${env.BUILD_NUMBER} @channel (<${env.RUN_DISPLAY_URL}|open job detail>)"
      )
    }
  }
}