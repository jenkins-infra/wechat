def isEmpty(str){
    return str == "" || str == null
}

pipeline {
    agent any

    stages{
        stage("文章格式校验"){
            steps{
                script{
                    withChangeSets(){
                        if(env.changePath != "" && env.changePath != null && env.changePath.endsWith(".md")) {
                            def articles = readYaml file: env.changePath
                            def article = articles[0]
                            if(isEmpty(article.title) || isEmpty(article.description)
                                || isEmpty(article.author) || isEmpty(article.poster)){
                                error "title, description, author or poster can not be empty"
                            }

                            if(!isEmpty(article.translator) && isEmpty(article.original)) {
                                error "current article is translated from the origin one, please provide the original link"
                            }
                        }
                    }
                }
            }
        }
    }
}

// DSL withChangeSets has not been released, see also
// https://github.com/LinuxSuRen/change-handler