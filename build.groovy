def stageResult = [:] // задаем массив куда будут сохранятся статусы выполнения шагов
def scmVars
def commitHash
def OSS_FLAG_STATUS

// Настройки для оберток maven
mavenVertsion = 'Maven 3.5.0'

String NEXUS_VERSION
String NEXUS_ARTIFACT = 'CI00355268_KKA'
String jdkName = 'JDK1.8'

String branch = GitBranch
echo "GitBranch: ${GitBranch}, branch: ${branch}"
String releaseVersion

node('linux') {

    cleanWs() // Очистка рабочего пространства

    wrap([$class: 'TimestamperBuildWrapper']) {
        wrap([$class: 'AnsiColorBuildWrapper']) {

            withMaven(jdk: jdkName, maven: mavenVertsion) {

                executeStage('Determine NEXUS_VERSION', branch, stageResult) {
                    // Находим NEXUS_VERSION
                    def pomModel = readMavenPom()
                    String versionPom = pomModel.getVersion()
                   String releaseVer = versionPom.replaceAll('-SNAPSHOT', '')
                    NEXUS_VERSION = releaseVer
                    println("versionPom: ${versionPom}, NEXUS_VERSION: ${NEXUS_VERSION}")
                }

                    executeStage('Build Distrib', branch, stageResult) {
                        // Переходим на выбранную ветку
                        sh "git status"
                        sh "mvn -version"
                        sh "mvn clean install"
                        archiveArtifacts 'BankApi.zip'
                    }

                    executeStage('Nexus Upload', branch, stageResult) {
                        // Загрузка билда в нексус
                        def kkaDistribFile = findFiles(glob: 'BankApi.zip')[0]
                        println("kkaDistribFile name: ${kkaDistribFile.name}, path: ${kkaDistribFile.path}, " +
                                "directory: ${kkaDistribFile.directory}, length: ${kkaDistribFile.length}")
                                echo "Deploy NEXUS_VERSION: ${NEXUS_VERSION}"
                                sh "mvn deploy:deploy-file -DgeneratePom=true -DartifactId=${NEXUS_ARTIFACT} -Dversion=${NEXUS_VERSION}" +
                                        "-Dpackaging=zip -Dfile=${kkaDistribFile.path} -Durl=https:nexus:8081 -Dclassifier=distrib"
                    }
            }
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
