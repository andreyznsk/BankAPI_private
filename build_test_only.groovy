def stageResult = [:] // задаем массив куда будут сохранятся статусы выполнения шагов
def scmVars
def commitHash
def OSS_FLAG_STATUS

// Настройки для оберток maven
def mvnHome

String NEXUS_VERSION
String NEXUS_ARTIFACT = 'BankAPI_0001'
String project_git_url_ssh = 'ssh://git@bitbucket:7999/bai/bankapi.git'
String project_git_url_https = 'http://bitbucket:7990/scm/bai/bankapi.git'
String JenkinsCredentialsId = 'ubnt'
String nexusReleasesURL = 'http://localhost:8081/repository/release_artifactory/'
String nexus_artifactory = 'nexus_artifactory'
String mavenVersion = '3.5.0'
String configXml = 'MySettings'
def GitBranch


if(GitBranch == null) {
    GitBranch = 'origin/'+ BITBUCKET_SOURCE_BRANCH
}

String branch = GitBranch.split('/')[-1]
echo "git pr: ${GitBranch}"
node('ubuntu') {


    executeStage('Clean WS', stageResult) {
        cleanWs() // Очистка рабочего пространства
    }

    executeStage('Checkout in Linux', stageResult) { // Чекаут
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
        executeStage('Unit_test', stageResult) {
            // Находим NEXUS_VERSION
            def pomModel = readMavenPom()
            String versionPom = pomModel.getVersion()
            echo "versionPom: ${versionPom}"
            currentBuild.description = "version: ${versionPom}"
            sh "mvn clean test -U -e"
        }

    }
}


def executeStage(stageName, stageResult, Closure task) {
    try {
        stage(stageName, task)
    } catch (e) {
        echo "${e.getMessage()}"
        throw e
    }
}
