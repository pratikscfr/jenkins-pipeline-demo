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
                sh "ls -l"
            }
        } // End of 'Init'

        stage('Call ReadWritePipeline') {
            steps {
                script {
                    try {
                        echo "------------------START----------------"
                        def read_write_build = build(job: "ReadWritePipeline",
                            propagate: true,
                            wait: true)
                        copyArtifacts(projectName: "ReadWritePipeline", 
                            selector: specific("${read_write_build.number}"),
                            filter: "output/*.txt",
                            target: "input");
                    } catch (Exception e) {
                        error ("FATAL:: Ran into an issue while Running job. Error: " + e.message)
                    }
                }
            }
        }

        stage('Test.py test') {
            steps {
                script {
                    try {
                        def REVPRO_URL = "revpro-m-d202303202307.revpro.cloud"
                        echo "${REVPRO_URL}"
                        sh """
                            echo 'Hello World'
                            python3 --version
                            python3 -m venv venv
                            source venv/bin/activate
                            python --version
                            yes | pip install selenium --quiet --exists-action i
                            python -c "import selenium; print(selenium.__version__)"
                            python test.py --URL \"${REVPRO_URL}\"
                            python login.py --URL \"${REVPRO_URL}\"
                            pip uninstall --yes selenium
                            deactivate
                            rm -r venv
                        """
                    } catch (Exception e) {
                        error ("Fatal: " + e.message)
                    }
                }
            }
        }

        stage('Call pipeline_1') {
            steps {
                script {
                    try {
                        sh '''
                        grep multiuser input/output/*.txt
                        '''
                        grep_result = sh (
                            script: 'grep \'Revpro URL\' input/output/*.txt',
                            returnStdout: true
                        )

                        grep_result_array = []
                        grep_result.split().each {
                            grep_result_array << it
                        }
                        echo "grep result: ${grep_result_array}"
                        echo "grep result: ${grep_result_array[3]}"
                        def test_pipeline_build = build(job: "TestPipeline1",
                            propagate: true,
                            wait: true,
                            parameters: [
                                [$class: 'StringParameterValue', name: 'TEST_STRING', value: "${params.TEST_STRING},TEST"],
                            ])
                    } catch (Exception e) {
                        error ("FATAL:: Ran into an issue while Running job. Error: " + e.message)
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
        cleanup {
            script {
                sh ("rm -rf *.*")
            }
        }
    }
}
