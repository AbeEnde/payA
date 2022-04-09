#!/usr/bin/env groovy

node {
    stage('checkout') {
        checkout scm
    }

    docker.image('jhipster/jhipster:v7.3.1').inside('-u jhipster -e GRADLE_USER_HOME=.gradle') {
        stage('check java') {
            bat "java -version"
        }

        stage('clean') {
            bat "chmod +x gradlew"
            bat "./gradlew clean --no-daemon"
        }
        stage('nohttp') {
            bat "./gradlew checkstyleNohttp --no-daemon"
        }

        stage('npm install') {
            bat "./gradlew npm_install -PnodeInstall --no-daemon"
        }
        stage('backend tests') {
            try {
                bat "./gradlew test integrationTest -PnodeInstall --no-daemon"
            } catch(err) {
                throw err
            } finally {
                junit '**/build/**/TEST-*.xml'
            }
        }

        stage('frontend tests') {
            try {
                bat "./gradlew npm_run_test -PnodeInstall --no-daemon"
            } catch(err) {
                throw err
            } finally {
                junit '**/build/test-results/TESTS-*.xml'
            }
        }

        stage('packaging') {
            bat "./gradlew bootJar -x test -Pprod -PnodeInstall --no-daemon"
            archiveArtifacts artifacts: '**/build/libs/*.jar', fingerprint: true
        }

        stage('quality analysis') {
            withSonarQubeEnv('sonar') {
                bat "./gradlew sonarqube --no-daemon"
            }
        }
    }

    def dockerImage
    stage('publish docker') {
        // A pre-requisite to this step is to setup authentication to the docker registry
        // https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin#authentication-methods
        bat "./gradlew bootJar jib -Pprod -PnodeInstall --no-daemon"
    }
}
