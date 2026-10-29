// CI pipeline of the Student API - Ramazan Ospan, IT2-2310
pipeline {
    agent { label 'docker' }          // built-in node with Docker CLI and access to the Docker daemon
    tools { jdk 'jdk-21' }            // JDK configured in Manage Jenkins > Tools
    triggers { pollSCM('H/2 * * * *') }   // check GitHub for new commits every 2 minutes
    options {
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '20'))
    }
    environment {
        STUDENT    = 'Ramazan Ospan'
        GROUP      = 'IT2-2310'
        IMAGE_NAME = 'ospan-ramazan-student-api'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                script {
                    env.APP_VERSION = sh(returnStdout: true, script: "grep -E \"^version = \" build.gradle | cut -d\"'\" -f2").trim()
                    env.IMAGE_TAG = "v${env.APP_VERSION}-ospan-ramazan"
                    // branch + build number: builds of different branches can run at the same time
                    env.CI_NET = "ospan-ramazan-ci-${env.BRANCH_NAME.replaceAll('[^A-Za-z0-9]', '-')}-${env.BUILD_NUMBER}"
                }
                echo "Student: ${STUDENT} (${GROUP})"
                echo "Branch: ${env.BRANCH_NAME}, version ${env.APP_VERSION}, image ${IMAGE_NAME}:${env.IMAGE_TAG}"
                sh 'git log -1 --format="Commit: %h %s (%an)"'
            }
        }
        stage('Build') {
            steps {
                sh 'chmod +x gradlew && ./gradlew --no-daemon clean bootJar'
            }
        }
        stage('Test') {
            steps {
                sh './gradlew --no-daemon test'
            }
            post {
                always { junit 'build/test-results/test/*.xml' }
            }
        }
        stage('Docker Build') {
            steps {
                sh 'docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .'
            }
        }
        stage('Verify') {
            steps {
                sh '''
                    docker image inspect ${IMAGE_NAME}:${IMAGE_TAG} \
                      --format "Image {{index .RepoTags 0}}: {{.Size}} bytes, created {{.Created}}"
                '''
                // smoke test: the new image starts and connects to PostgreSQL; DB login comes from Jenkins Credentials
                withCredentials([usernamePassword(credentialsId: 'ospan-ramazan-db',
                                                  usernameVariable: 'DB_USER', passwordVariable: 'DB_PASSWORD')]) {
                    sh '''
                        set -e
                        NET=$CI_NET
                        docker network create $NET
                        docker run -d --name $NET-db --network $NET -e POSTGRES_DB=students \
                          -e POSTGRES_USER="$DB_USER" -e POSTGRES_PASSWORD="$DB_PASSWORD" postgres:16-alpine
                        until docker exec $NET-db pg_isready -U "$DB_USER" -d students; do sleep 2; done
                        docker run -d --name $NET-app --network $NET -e DB_URL=jdbc:postgresql://$NET-db:5432/students \
                          -e DB_USER="$DB_USER" -e DB_PASSWORD="$DB_PASSWORD" ${IMAGE_NAME}:${IMAGE_TAG}
                        for i in $(seq 1 45); do
                          docker exec $NET-app curl -fs localhost:8080/actuator/health && break
                          sleep 2
                        done
                        echo
                        docker exec $NET-app curl -fs localhost:8080/
                        echo
                        echo "Verify OK: ${IMAGE_NAME}:${IMAGE_TAG} is running and healthy"
                    '''
                }
            }
            post {
                always {
                    sh '''
                        NET=$CI_NET
                        docker logs --tail 5 $NET-app 2>/dev/null || true
                        docker rm -f $NET-app $NET-db > /dev/null 2>&1 || true
                        docker network rm $NET > /dev/null 2>&1 || true
                    '''
                }
            }
        }
    }

    post {
        success { echo "Pipeline of ${STUDENT}: SUCCESS, image ${IMAGE_NAME}:${env.IMAGE_TAG}" }
        failure { echo "Pipeline of ${STUDENT}: FAILED - see the red stage above" }
    }
}
