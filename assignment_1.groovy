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
            }
        } // End of 'Init'

        stage('Call pipeline_1') {
            steps {
                script {
                    try {
                        def self_service_build = build(job: "TestPipeline1",
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
        cleanup {
            script {
                bat ''' 
                    cd ..
                    echo %cd%
                    rmdir /s /q .
                    '''
            }
        }
    }
}
