pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
        jdk 'JDK-17'
    }

    environment {
        DOCKER_IMAGE = "spinwheel-backend"
        DOCKER_TAG = "${env.BUILD_NUMBER}"
    }

    stages {
        stage('Build') {
            steps {
                sh 'mvn clean compile -B'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test -B'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests -B'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Docker Build') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                sh "docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest"
            }
        }

        stage('Deploy to QA') {
            steps {
                echo "Deploying ${DOCKER_IMAGE}:${DOCKER_TAG} to QA environment"
                // Replace with actual deployment command for your environment
                // sh "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG}"
                // sh "kubectl set image deployment/spinwheel-backend spinwheel-backend=${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG}"
            }
        }
    }

    post {
        success {
            echo "Build ${env.BUILD_NUMBER} succeeded"
        }
        failure {
            echo "Build ${env.BUILD_NUMBER} failed"
        }
    }
}
