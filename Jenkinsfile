pipeline {
  agent any

  options {
    timestamps()
    disableConcurrentBuilds()
  }

  environment {
    DOCKERHUB_NAMESPACE = 'swathich1'
    DOCKERHUB_CREDENTIALS_ID = 'dockerhub'
    SERVICES = "user-service order-service notification-service"
    COMPOSE_FILE = 'docker-compose.yml'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
        script {
          env.GIT_COMMIT_SHORT = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
          echo "Building commit ${env.GIT_COMMIT_SHORT}"
        }
      }
    }

    stage('Build (Maven)') {
      parallel {
        stage('user-service') {
          steps {
            sh """
              cd user-service
              mvn -B clean package -DskipTests
            """
          }
        }
        stage('order-service') {
          steps {
            sh """
              cd order-service
              mvn -B clean package -DskipTests
            """
          }
        }
        stage('notification-service') {
          steps {
            sh """
              cd notification-service
              mvn -B clean package -DskipTests
            """
          }
        }
      }
    }

    stage('Docker Build & Tag') {
      steps {
        sh """
          set -e
          for S in ${SERVICES}; do
            echo "Building image for \$S ..."
            docker build -t ${DOCKERHUB_NAMESPACE}/\${S}:latest -t ${DOCKERHUB_NAMESPACE}/\${S}:${GIT_COMMIT_SHORT} ${S}
          done
        """
      }
    }

    stage('Docker Login & Push') {
      steps {
       withCredentials([usernamePassword(credentialsId: "dockerhub", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
          sh """
             echo "${DOCKER_PASS}" | docker login -u "${DOCKER_USER}" --password-stdin
             for S in ${SERVICES}; do
              docker push ${DOCKERHUB_NAMESPACE}/\${S}:latest
              docker push ${DOCKERHUB_NAMESPACE}/\${S}:${GIT_COMMIT_SHORT}
            done
            docker logout
          """
        }
      }
    }

    stage('Deploy (docker-compose)') {
      steps {
        sh """
          # Uses images tagged 'latest' by default as referenced in docker-compose.yml
          docker-compose down || docker compose down
	  docker-compose up -d || docker compose up -d

        """
      }
    }

    stage('Sanity Check') {
      steps {
        sh """
          for url in 8081 8082 8083; do
  		echo "Checking service at port $url ..."
  		for i in {1..5}; do
    		   if curl -sf http://localhost:$url/actuator/health; then
      			echo "Service on $url is healthy"
      			break
    		   fi
    		   echo "Retry in 5s..."
    		   sleep 5
  	        done
	  done

        """
      }
    }
  }

  post {
    success {
      echo "Pipeline succeeded. Images pushed with tags: latest, ${env.GIT_COMMIT_SHORT}"
    }
    failure {
      echo "Pipeline failed. Check the stage logs."
    }
    always {
      echo "Cleaning up or archiving artifacts if needed."
    }
  }
}

