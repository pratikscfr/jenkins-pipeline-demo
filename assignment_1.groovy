pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Build World'
            }
        }
	stage('Test') {
            steps {
                echo 'Test World'
            }
        }
	stage('Deploy') {
            steps {
                echo 'Deploy World'
            }
        }
    }
}
