def stageResult = [:] // задаем массив куда будут сохранятся статусы выполнения шагов
def scmVars
def commitHash
def OSS_FLAG_STATUS

// Настройки для оберток maven
def mvnHome

String NEXUS_VERSION
String NEXUS_ARTIFACT = 'BankAPI_0001'
String project_git_url_ssh = 'git@bitbucket:7999/bai/bankapi.git'
String project_git_url_https = 'http://bitbucket:7990/scm/bai/bankapi.git'
String JenkinsCredentialsId = 'ubnt'
String nexusReleasesURL = 'http://localhost:8081/repository/release_artifactory/'
String nexus_artifactory = 'nexus_artifactory'
String mavenVersion = '3.5.0'
String configXml = 'MySettings'

String branch = GitBranch.split('/')[-1]
node('ubuntu') {


    executeStage('Clean WS', branch, stageResult) {
        cleanWs() // Очистка рабочего пространства
    }

    executeStage('Checkout in Linux', branch, stageResult) { // Чекаут
        scmVars = checkout([$class                           : 'GitSCM',
                            branches                         : [[name: "${GitBranch}"]],
                            browser                          : [$class: 'BitbucketWeb', repoUrl: project_git_url_https],
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
        executeStage('Determine NEXUS_VERSION', branch, stageResult) {
            // Находим NEXUS_VERSION
            def pomModel = readMavenPom()
            String versionPom = pomModel.getVersion()
            echo "versionPom: ${versionPom}"
            String releaseVersion = versionPom.replaceAll('-SNAPSHOT', '')
            NEXUS_VERSION = releaseVersion
            echo "NEXUS_VERSION: ${NEXUS_VERSION}"
            currentBuild.description = "#${BUILD_NUMBER}, version: ${NEXUS_VERSION}"
        }

        executeStage('Build Distrib', branch, stageResult) {
            withCredentials([file(credentialsId: 'mavenSettingsSecurity', variable: 'MavenSettingsSecurityFile')]) {
                sshagent([JenkinsCredentialsId]) {
                    String fileContent = readFile encoding: 'UTF-8', file: "${MavenSettingsSecurityFile}"
                    String mss = pwd() + '/security/mvn_ss.xml'
                    echo "security path: ${mss}"
                    writeFile file: mss, text: fileContent
                    sh 'git config --global user.email "you@example.com"'
                    sh 'git config --global user.name "jenkins"'
                    String nextVersion = String.format('%03d', Integer.parseInt(NEXUS_VERSION.split('\\.')[1]) + 1)
                    echo "next build number: ${nextVersion}"
                    sh "mvn release:clean release:prepare " +
                            " -DautoVersionSubmodules=true -DdevelopmentVersion=${nextVersion} " +
                            " -DreleaseVersion=${NEXUS_VERSION} -Dtag=BankApi-${NEXUS_VERSION} -B -e"
                    sh "mvn release:perform -Darguments=\"-Dsettings.security=${mss} -DargLine=-DdbUserSuffix=BLD\" -B -e"
                    dir('Service/BankAPIMain/') {
                        sh "zip -r database.zip . -i database/*.sql"
                        sh "zip -r bankAPI.zip database.zip target/BankAPI.jar"
                    }
                    archiveArtifacts 'Service/BankAPIMain/bankAPI.zip'
                }
            }
        }

        executeStage('upload to NEXUS', branch, stageResult) {
            // Загрузка билда в нексус
            withCredentials([usernamePassword(credentialsId: "admin", usernameVariable: "nexusUser", passwordVariable: "nexusPwd")]) {
                sh "pwd"
                def bankAipFile = findFiles(glob: 'Service/BankAPIMain/bankAPI.zip')[0]
                echo "Deploy NEXUS_VERSION: ${NEXUS_VERSION}"
                echo "bankAipFile name: ${bankAipFile.name}, path: ${bankAipFile.path}, dir: ${bankAipFile.directory}"
                //sh "git checkout BankApi-${NEXUS_VERSION}"
                sh "git status"
                sh "mvn deploy:deploy-file -DgeneratePom=true -DartifactId=${NEXUS_ARTIFACT} -Dversion=${NEXUS_VERSION}" +
                        " -Dpackaging=zip -Dfile=${bankAipFile.path} -Durl=${nexusReleasesURL} -DgroupId=NEXUS_PROD" +
                        " -Drepo.usr=${nexusUser} -Drepo.pwd=${nexusPwd} -Dclassifier=distrib -DrepositoryId=${nexus_artifactory} -e"
            }
        }


//    executeStage('Docker build', branch, stageResult) {
//        // Загрузка сборка Докер Образ
//        sh 'docker build . -t bankapi'
//     }
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
