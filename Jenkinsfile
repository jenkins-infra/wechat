pipeline {
    agent {
        label 'linux'
    }

    options {
        disableConcurrentBuilds()
        quietPeriod 30
    }

    stages{
        stage("Notify"){
            when {
                branch 'master'
            }
            steps{
                withCredentials([string(credentialsId: '0404af2d-8738-402e-922d-5acee65d3059', variable: 'TOKEN')]) {
                    sh '''
                        curl -X POST -H "Accept: application/vnd.github.everest-preview+json" -H "Authorization: token ${TOKEN}" -i "https://api.github.com/repos/jenkins-zh/jenkins-zh/dispatches" -d '{"event_type":"repository_dispatch"}'
                    '''
                }
            }
        }
    }
}
