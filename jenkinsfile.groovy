def stageResult = [:] // задаем массив куда будут сохранятся статусы выполнения шагов
def scmVars
def commitHash
def OSS_FLAG_STATUS

// Настройки для оберток maven
def mvnHome

String NEXUS_VERSION
String project_git_url = 'git@github.com:andreyznsk/BankAPI_private.git'
String JenkinsCredentialsId = 'ubnt'

String branch = GitBranch
echo "GitBranch: ${GitBranch}, branch: ${branch}"

node('ubuntu') {


    executeStage('Clean WS', branch, stageResult) {
        cleanWs() // Очистка рабочего пространства
    }

    executeStage('Checkout in Linux', branch, stageResult) { // Чекаут
        scmVars = checkout(
                [$class       : 'GitSCM',
                 branches                         : [[name: "${GitBranch}"]],
                 browser                          : [$class: 'GitWeb', repoUrl: 'https://github.com/andreyznsk/BankAPI_private.git'],
                 doGenerateSubmoduleConfigurations: false,
                 extensions                       : [[$class: 'CleanCheckout'], [$class: 'LocalBranch', localBranch: branch]],
                 gitTool                          : 'Default',
                 submoduleCfg                     : [],
                 userRemoteConfigs                : [[credentialsId: JenkinsCredentialsId, url: project_git_url]]
                ])
    }

    executeStage('Determine NEXUS_VERSION', branch, stageResult) {
        mvnHome = tool '3.5.0'
        // Находим NEXUS_VERSION
        String versionPom = sh script: "'${mvnHome}/bin/mvn' help:evaluate -Dexpression=project.version -q -DforceStdout", returnStdout: true
        echo "versionPom: ${versionPom}"
        String releaseVer = versionPom.replaceAll('-SNAPSHOT', '')
        NEXUS_VERSION = releaseVer
        echo "NEXUS_VERSION: ${NEXUS_VERSION}"
    }

    executeStage('Build Distrib', branch, stageResult) {
        // Переходим на выбранную ветку
        sh "pwd"
        sh "git checkout ${GitBranch}"
        sh "whoami"
        sh "'${mvnHome}/bin/mvn' --version"
        sh "'${mvnHome}/bin/mvn' clean install"
        sh "zip -r database.zip . -i database/*.sql"
        archiveArtifacts 'BankAPIMain/database.zip'
        archiveArtifacts 'BankAPIMain/target/BankAPI.jar'
    }

    executeStage('Docker build', branch, stageResult) {
        // Загрузка билда в нексус
        echo "Deploy NEXUS_VERSION: ${NEXUS_VERSION}"
        sh 'docker build . -t bankapi'
        /*println("kkaDistribFile name: ${kkaDistribFile.name}, path: ${kkaDistribFile.path}, " +
                "directory: ${kkaDistribFile.directory}, length: ${kkaDistribFile.length}")
        echo "Deploy NEXUS_VERSION: ${NEXUS_VERSION}"
        sh "mvn deploy:deploy-file -DgeneratePom=true -DartifactId=${NEXUS_ARTIFACT} -Dversion=${NEXUS_VERSION}" +
                "-Dpackaging=zip -Dfile=${kkaDistribFile.path} -Durl=https:nexus:8081 -Dclassifier=distrib"*/
    }

}

def executeStage(stageName, branch, stageResult, Closure task) {
    try {
        stage(stageName, task)
    } catch (e) {
        echo "${e.getMessage()}"
        cleanWs()
        throw e
    }
}
