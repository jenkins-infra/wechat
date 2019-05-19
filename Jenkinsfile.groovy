pipeline {
    agent any

    stages{
        stage("文章格式校验"){
            when {
                changeRequest target: 'master'
            }

            steps{
                script{
                    withChangeSets(){
                        if(env.changePath != "" && env.changePath != null && env.changePath.endsWith(".md")) {
                            checkArticleMeta(env.changePath)
                        }
                    }
                }
            }
        }
        stage("preview"){
            when {
                changeRequest target: 'master'
            }

            steps{
                script{
                    archiveArtifacts '.'
                    def branch = "wechat-$BRANCH_NAME"
                    branch = branch.toLowerCase()
                    build job: 'jenkins-zh/jenkins-zh/master', parameters: [string(name: 'previewUpstream', value: branch)]
                    pullRequest.createStatus(status: 'success',
                        context: 'continuous-integration/jenkins/pr-merge/preview',
                        description: 'Website preview',
                        targetUrl: "http://" + branch + ".preview.jenkins-zh.cn")
                }
            }
        }
    }
}

def checkArticleMeta(filePath){
    def articleText = readFile encoding: 'UTF-8', file: filePath
    def articleYamlText = getYamlMeta(articleText)
    if(isEmpty(articleYamlText)){
        error "cannot find the yaml meta from current article"
    }

    def articles = readYaml file: articleYamlText
    def article = articles[0]
    if(isEmpty(article.title) || isEmpty(article.description)
        || isEmpty(article.author) || isEmpty(article.poster)){
        error "title, description, author or poster can not be empty"
    }

    if(!isEmpty(article.translator) && isEmpty(article.original)) {
        error "current article is translated from the origin one, please provide the original link"
    }
}

def isEmpty(str){
    return str == "" || str == null
}

def getYamlMeta(txt){
    def firstIndex = txt.indexOf('---')
    if(firstIndex == -1){
        return ""
    }
    def secondIndex = txt.indexOf('---', firstIndex + 3)
    if(secondIndex == -1){
        return ""
    }
    return txt.substring(firstIndex, secondIndex + 3)
}

// DSL withChangeSets has not been released, see also
// https://github.com/LinuxSuRen/change-handler