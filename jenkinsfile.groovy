def stageResult = [:] // задаем массив куда будут сохранятся статусы выполнения шагов
def scmVars
def commitHash
def OSS_FLAG_STATUS

// Настройки для оберток maven
def mvnHome

String NEXUS_VERSION
String NEXUS_ARTIFACT = 'BankAPI_0001'
String project_git_url_ssh = 'git@github.com:andreyznsk/BankAPI_private.git'
String project_git_url_https = 'https://github.com/andreyznsk/BankAPI_private.git'
String JenkinsCredentialsId = 'ubnt'
String nexusReleasesURL = 'http://localhost:8081/repository/maven-releases/'
String nexusRepoId = 'maven-releases'

String branch = GitBranch.split('/')[-1]
node('ubuntu') {


    executeStage('Clean WS', branch, stageResult) {
        cleanWs() // Очистка рабочего пространства
    }

    executeStage('Checkout in Linux', branch, stageResult) { // Чекаут
        scmVars = checkout(
                [$class                           : 'GitSCM',
                 branches                         : [[name: "${GitBranch}"]],
                 browser                          : [$class: 'GitWeb', repoUrl: project_git_url_https],
                 doGenerateSubmoduleConfigurations: false,
                 extensions                       : [[$class: 'CleanCheckout'], [$class: 'LocalBranch', localBranch: branch]],
                 gitTool                          : 'Default',
                 submoduleCfg                     : [],
                 userRemoteConfigs                : [[credentialsId: JenkinsCredentialsId, url: project_git_url_ssh]]
                ])

        echo "scmVars: ${scmVars}"
        echo "GitBranch: ${GitBranch}, branch: ${branch}"
    }

    executeStage('Determine NEXUS_VERSION', branch, stageResult) {
        mvnHome = tool '3.5.0'
        // Находим NEXUS_VERSION
        String versionPom = sh script: "'${mvnHome}/bin/mvn' help:evaluate -Dexpression=project.version -q -DforceStdout", returnStdout: true
        echo "versionPom: ${versionPom}"
        String releaseVersion = versionPom.replaceAll('-SNAPSHOT', '')
        NEXUS_VERSION = releaseVersion
        echo "NEXUS_VERSION: ${NEXUS_VERSION}"
    }

    executeStage('Build Distrib', branch, stageResult) {
        sshagent([JenkinsCredentialsId]) {

            sh 'git config --global user.email "you@example.com"'
            sh 'git config --global user.name "jenkins"'
            sh "'${mvnHome}/bin/mvn' --version"
            String nextVersion = String.format('%03d', Integer.parseInt(NEXUS_VERSION.split('\\.')[1]) + 1)
            echo "next build number: ${nextVersion}"
            sh "'${mvnHome}/bin/mvn' release:clean release:prepare " +
                    "--batch-mode -DautoVersionSubmodules=true -DdevelopmentVersion=${nextVersion} " +
                    "-DreleaseVersion=${NEXUS_VERSION} -Dtag=BankApi-${NEXUS_VERSION} -e"
            dir('BankAPIMain/') {
                sh "zip -r database.zip . -i database/*.sql"
                sh "zip -r bankAPI.zip database.zip target/BankAPI.jar"
            }
            archiveArtifacts 'BankAPIMain/database.zip'
            archiveArtifacts 'BankAPIMain/target/BankAPI.jar'
        }
    }

    executeStage('upload to nexus', branch, stageResult) {
        // Загрузка билда в нексус
        withCredentials([usernamePassword(credentialsId: "nexusUser", usernameVariable: "nexusUser", passwordVariable: "nexusPwd")]) {
            def bankAipFile = findFiles(glob: 'BankAPIMain/bankAPI.zip')[0]
            echo "Deploy NEXUS_VERSION: ${NEXUS_VERSION}"
            echo "bankAipFile {name: ${bankAipFile.name}, path: ${bankAipFile.path}, dir: ${bankAipFile.directory}"
            sh "'${mvnHome}/bin/mvn' deploy:deploy-file -DgroupId=Nexus_PROD -DgeneratePom=true -DartifactId=${NEXUS_ARTIFACT} -Dversion=${NEXUS_VERSION}" +
                    "-Dpackaging=zip -Dfile=${bankAipFile.path} -DrepositoryId=${nexusRepoId} -Durl=${nexusReleasesURL}" +
                    "-Drepo.username=${nexusUser} -Drepo.password=${nexusPwd} -Dclassifier=distrib"
        }
    }


//    executeStage('Docker build', branch, stageResult) {
//        // Загрузка сборка Докер Образ
//        sh 'docker build . -t bankapi'
//     }

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
