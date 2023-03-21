pipeline {
    options {
        timestamps()
    }

    agent any

    stages {

        stage('Init') {
            steps {
                echo 'Init'
            }
        }

        stage('Create build output') {
            steps {
                // Make the output directory.
                sh "mkdir -p output"

                // Write an useful file, which is needed to be archived.
                writeFile file: "output/usefulfile.txt", text: '''This file is useful, need to archive it.
                                                                unix is great os. unix was developed in Bell labs.
                                                                learn operating system.
                                                                Unix linux which one you choose.
                                                                uNix is easy to learn.unix is a multiuser os.Learn unix .unix is a powerful.'''

                // Write an useless file, which is not needed to be archived.
                writeFile file: "output/uselessfile.md", text: "This file is useless, no need to archive it."
            }
        }

        stage('Archive build output') {
            steps {
                // Archive the build output artifacts.
                archiveArtifacts artifacts: 'output/*.txt', excludes: 'output/*.md'
            }
        }
    }
}
