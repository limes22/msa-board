import static java.util.UUID.randomUUID
def uuid = randomUUID() as String
def myid = uuid.take(8)

pipeline {
  environment {
    APP_VER = "v1.0.${BUILD_ID}"
    // HARBOR_URL = ""
    DEPLOY_GITREPO_USER = "limes22"
    DEPLOY_GITREPO_URL = "github.com/limes22/msa-board.git"
    DEPLOY_GITREPO_BRANCH = "master"
    DEPLOY_GITREPO_TOKEN = credentials('devops-github')
    repository = "howdi2000/edu-msa-board"
    DOCKERHUB_CREDENTIALS = credentials('docker-hub')
  }

  stages {
    stage('Build') {
      steps {
        container('maven') {
          echo sh(script: 'env|sort', returnStdout: true)
          sh """
            mvn package -f pom.xml
            """
        }
      }
    }
  }
}

