pipeline {
    agent any
    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials')
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Swathi-dev-coder/order-management-system'
            }
        }
        stage('Build Jars') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('Build & Push Docker Images') {
            steps {
                script {
                    def services = [
                        [name: "order-service", image: "swathich1/order-service"],
                        [name: "user-service", image: "swathich1/user-service"],
                        [name: "notification-service", image: "swathich1/notification-service"]
                    ]
                    sh "echo ${DOCKERHUB_CREDENTIALS_PSW} | docker login -u ${DOCKERHUB_CREDENTIALS_USR} --password-stdin"
                    for (srv in services) {
                        sh "docker build -t ${srv.image}:latest ./\${srv.name}"
                        sh "docker push ${srv.image}:latest"
                    }
                }
            }
        }
        stage('Deploy') {
            steps {
                sh "docker-compose down && docker-compose up -d"
            }
        }
    }
}
