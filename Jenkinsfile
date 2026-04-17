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

        stage('Deploy to Railway') {
            steps {
                echo "Deploying ${DOCKER_IMAGE}:${DOCKER_TAG} to Railway"
                // Uncomment and configure once Railway is connected to Jenkins:
                // 1. Add RAILWAY_TOKEN as a Jenkins credential (Secret text)
                // 2. Set RAILWAY_SERVICE_ID in environment block above
                //
                // withCredentials([string(credentialsId: 'railway-token', variable: 'RAILWAY_TOKEN')]) {
                //     sh '''
                //         curl -s -X POST "https://backboard.railway.com/graphql/v2" \
                //             -H "Authorization: Bearer ${RAILWAY_TOKEN}" \
                //             -H "Content-Type: application/json" \
                //             -d '{"query":"mutation { serviceInstanceRedeploy(serviceId: \\"'"${RAILWAY_SERVICE_ID}"'\\") }"}'
                //     '''
                // }
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
