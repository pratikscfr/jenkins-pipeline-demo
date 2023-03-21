pipeline {
    options {
        timestamps()
    }

    agent any //run this pipeline on any available agent ..... what to use here and how to know it

    parameters {
        string(name: "TEST_STRING", defaultValue: "develop", description: "The branch on which you want to check:")
    }

    stages {
        stage('Init') {
            steps {
                echo "Initializing Pipeline"
                sh "mkdir -p input"
            }
        } // End of 'Init'

        stage('Call ReadWritePipeline') {
            steps {
                script {
                    try {
                        def read_write_build = build(job: "ReadWritePipeline",
                            propagate: true,
                            wait: true)
                        copyArtifacts(projectName: "ReadWritePipeline", 
                            selector: specific("${read_write_build.number}"),
                            filter: "output/*.txt",
                            target: "input");
                    } catch (Exception e) {
                        'error ("FATAL:: Ran into an issue while Running job. Error: " + e.message)'
                    }
                }
            }
        }

        stage('Call pipeline_1') {
            steps {
                script {
                    try {
                        def var = sh "grep 'unix' input/*.txt"
                        def test_pipeline_build = build(job: "TestPipeline1",
                            propagate: true,
                            wait: true,
                            parameters: [
                                [$class: 'StringParameterValue', name: 'TEST_STRING', value: "${params.TEST_STRING}"],
                            ])
                    } catch (Exception e) {
                        'error ("FATAL:: Ran into an issue while Running job. Error: " + e.message)'
                    }
                }
            }
        } // Do we only mean build a downstream job/pipeline when we say call another pieline ?
    }

    post {
        always {
            script {
                echo 'Printing'
            }
        }
        success {
            script {
                echo 'Success'
            }
        }
        failure {
            script {
                echo 'Failed'
            }
        }
    }
}
