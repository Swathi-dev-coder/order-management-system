pipeline {
  agent any

  options {
    timestamps()
    disableConcurrentBuilds()
  }

  environment {
    DOCKERHUB_NAMESPACE = 'swathich1'
    DOCKERHUB_CREDENTIALS_ID = 'dockerhub'
    GIT_COMMIT_SHORT = ''
    SERVICES = "userservice order-service notification-service"
    SERVICE_IMAGE_MAP = "userservice:user-service order-service:order-service notification-service:notification-service"
    COMPOSE_FILE = 'docker-compose.yml'
  }
  tools {
        maven 'Maven 3'   // You'll need to install Maven via Jenkins Global Tool Config
        jdk 'JDK 17'      // Also install JDK in Jenkins
  }
  stages {

    stage('Cleanup Workspace') {
      steps {
        echo 'Cleaning workspace...'
        deleteDir()
      }
    }

    // stage('Clone Repository') {
    //   steps {
    //     sh """
    //       rm -rf order-management-system
    //       git clone https://github.com/Swathi-dev-coder/order-management-system
    //     """
    //   }
    // }

    // stage('Get Commit Hash') {
    //   steps {
    //       dir('order-management-system') {
    //         script {
    //           env.GIT_COMMIT_SHORT = sh(
    //             script: "git rev-parse --short HEAD",
    //             returnStdout: true
    //           ).trim()
    //           echo "Building commit ${env.GIT_COMMIT_SHORT}"
    //         }
    //       }
    //   }
    // }
    stage('Checkout Repository') {
      steps {
        deleteDir() // Clean workspace
        checkout([$class: 'GitSCM',
          branches: [[name: '*/main']],
          userRemoteConfigs: [[url: 'https://github.com/Swathi-dev-coder/order-management-system.git']]
        ])
      }
    }

    stage('Get Commit Hash') {
      steps {
        script {
          env.GIT_COMMIT_SHORT = sh(
            script: "git rev-parse --short HEAD",
            returnStdout: true
          ).trim()
          echo "Building commit ${env.GIT_COMMIT_SHORT}"
        }
      }
    }


    stage('Build (Maven)') {
      parallel {
        stage('userservice') {
          steps {
              dir('order-management-system/userservice/userservice'){
                  sh 'mvn -B clean package -DskipTests'
              }
          }
        }
        stage('order-service') {
          steps {
            dir('order-management-system/order-service/order-service') {
              sh 'mvn -B clean package -DskipTests'
            }
          }
        }
        stage('notification-service') {
          steps {
            dir('order-management-system/notification-service/notification-service') {
              sh 'mvn -B clean package -DskipTests'
            }
          }
        }
      }
    }
    stage('Docker Build & Tag') {
        steps {
            script {
                def commit = sh(script: "git -C order-management-system rev-parse --short HEAD", returnStdout: true).trim()
                echo "Commit hash is: ${commit}"
                for (mapping in SERVICE_IMAGE_MAP.split()) {
                    def folder = mapping.split(':')[0]
                    def image = mapping.split(':')[1]
                   sh """
                      docker build -t ${DOCKERHUB_NAMESPACE}/${image}:latest \
                                   -t ${DOCKERHUB_NAMESPACE}/${image}:${env.GIT_COMMIT_SHORT} \
                                   order-management-system/${folder}/${folder}
                    """

                }
            }
        }
    }

    stage('Docker Login & Push') {
        steps {
            script {
                withCredentials([usernamePassword(
                    credentialsId: "${DOCKERHUB_CREDENTIALS_ID}",
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    // Login once
                    sh 'echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin'

                    // Push all images
                    for (mapping in SERVICE_IMAGE_MAP.split()) {
                        def image = mapping.split(':')[1]
                        def commit = env.GIT_COMMIT_SHORT
                        echo "Pushing Docker image ${image} with tags latest and ${commit}"
                        sh """
                            docker push ${DOCKERHUB_NAMESPACE}/${image}:${env.GIT_COMMIT_SHORT}
                            docker push ${DOCKERHUB_NAMESPACE}/${image}:latest
                        """
                    }

                    // Logout once
                    sh 'docker logout'
                }
            }
        }
    }
    stage('Deploy (docker-compose)') {
      steps {
           dir('order-management-system') {
 	      script {
               	  echo "📂 Current Jenkins workspace path: ${pwd()}"
	      
                  if (fileExists(env.COMPOSE_FILE)) {
                    echo "Found ${env.COMPOSE_FILE}, running docker-compose"                
                    sh """
                          # If docker-compose installed
                          docker-compose -f ${COMPOSE_FILE} down || true
                          docker-compose -f ${COMPOSE_FILE} up -d || true
                        """
                  }else {
                            echo "⚠️ ${env.COMPOSE_FILE} not found in : ${pwd()}"
                            sh "ls -la"
                        }
                   
                }
          }
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