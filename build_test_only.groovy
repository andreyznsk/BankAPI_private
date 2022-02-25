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
String nexusReleasesURL = 'http://localhost:8081/repository/release_artifactory/'
String nexus_artifactory = 'nexus_artifactory'
String mavenVersion = '3.5.0'
String configXml = 'MySettings'

echo "TEST - 1"

String branch = GitPullRequest.split('/')[-1]
echo "git pr: ${GitPullRequest}"
node('ubuntu') {


    executeStage('Clean WS', branch, stageResult) {
        cleanWs() // Очистка рабочего пространства
    }

    executeStage('Checkout in Linux', branch, stageResult) { // Чекаут
        scmVars = checkout([$class                           : 'GitSCM',
                            branches                         : [[name: "${GitBranch}"]],
                            browser                          : [$class: 'GitWeb', repoUrl: project_git_url_https],
                            doGenerateSubmoduleConfigurations: false,
                            extensions                       : [[$class: 'CleanCheckout'], [$class: 'LocalBranch', localBranch: branch]],
                            gitTool                          : 'Default',
                            submoduleCfg                     : [],
                            userRemoteConfigs                : [[credentialsId: JenkinsCredentialsId, url: project_git_url_ssh]]])

        echo "scmVars: ${scmVars}"
        echo "GitBranch: ${GitBranch}, branch: ${branch}"
    }
    withMaven(maven: mavenVersion, mavenSettingsConfig: configXml,
            options: [artifactsPublisher(disabled: true)]) {
        executeStage('Unit_test', branch, stageResult) {
            // Находим NEXUS_VERSION
            def pomModel = readMavenPom()
            String versionPom = pomModel.getVersion()
            echo "versionPom: ${versionPom}"
            currentBuild.description = "version: ${versionPom}"
            sh "mvn clean test -U -e"
        }

    }
}


def executeStage(stageName, branch, stageResult, Closure task) {
    try {
        stage(stageName, task)
    } catch (e) {
        echo "${e.getMessage()}"
        throw e
    }
}
