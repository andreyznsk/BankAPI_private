@Library('DPMPipelineUtils@1.4') _

def vaultPasswordFileCredentialsId = 'file_pass_vault' // Jenkins_ID с файлом vault в котором зашифрованы необходимые пароли для Ansible
def stageResult = [:] // задаем массив куда будут сохранятся статусы выполнения шагов
def scmVars
def commitHash
def OSS_FLAG_STATUS
String distribNexusLink

// Настройки для оберток maven
mavenVertsion = 'Maven 3.5.4'
mavenSettingsConfig = 'KKA-repo'
mavenSettingsSecurity = 'maven-settings-security' // Jenkins ID с конфигурацией в xml для maven
JenkinsCredentialsId = 'KKA_Jenkins_ssh_ID'  // Jenkins ID с ssh ключом для доступа к Bitbucket (CI и CDL)

String NEXUS_VERSION
String NEXUS_ARTIFACT = 'CI00355268_KKA'
String jdkName = 'JDK1.8.0_171'

String branch = GitBranch.split('/')[-1]
echo "GitBranch: ${GitBranch}, branch: ${branch}, recipients: ${recipients}"
String project_git_url = 'ssh://git@stash.delta.sbrf.ru:7999/kka/kka.git'
String releaseVersion

//node('opir_uats_slave_linux_05') {
node('Linux_Default') {
//node('clearAgent') {

    cleanWs() // Очистка рабочего пространства

    wrap([$class: 'TimestamperBuildWrapper']) {
        wrap([$class: 'AnsiColorBuildWrapper']) {

            notifyStartBuild(branch, recipients) // Отправка письма о начале сборки

            executeStage('Checkout in Linux_Default', branch, stageResult, recipients) { // Чекаут
                scmVars = checkout(
                        [$class                           : 'GitSCM',
                         branches                         : [[name: "${GitBranch}"]],
                         browser                          : [$class: 'BitbucketWeb', repoUrl: 'https://stash.delta.sbrf.ru/projects/KKA/repos/kka'],
                         doGenerateSubmoduleConfigurations: false,
                         extensions                       : [[$class: 'CleanCheckout'], [$class: 'LocalBranch', localBranch: branch]],
                         gitTool                          : 'Default',
                         submoduleCfg                     : [],
                         userRemoteConfigs                : [[credentialsId: JenkinsCredentialsId, url: project_git_url]]]
                )
            }

            withMaven(jdk: jdkName, maven: mavenVertsion, mavenSettingsConfig: mavenSettingsConfig) {
                withCredentials([file(credentialsId: mavenSettingsSecurity, variable: 'MavenSettingsSecurityFile')]) {
				
				// Собираем дистрибутив
                    executeStage('Release to sbtNexus', branch, stageResult, recipients) {
                        // Переходим на выбранную ветку
                        sshagent([JenkinsCredentialsId]) {
                            sh 'git config user.name jenkins'
                            def pomModel = readMavenPom()
                            String version = pomModel.getVersion()
                            releaseVersion = version.replaceAll('-SNAPSHOT', '')
                            echo "version: ${version}, releaseVersion: ${releaseVersion}"
                            String nextBuildNumber = String.format('%03d', version.split('-')[1][1..-1].toInteger() + 1)
                            echo "next nextBuildNumber: ${nextBuildNumber}"
                            String nextVersion = version.replaceAll('b[\\d]+', 'b' + nextBuildNumber)
                            String fileContent = readFile encoding: 'UTF-8', file: "${MavenSettingsSecurityFile}"
                            String mss = pwd() + '/infrastructure/kka-deploy/mvn_ss.xml'
                            writeFile file: mss, text: fileContent
                            echo "next version: ${nextVersion}"
                            sh "mvn -Darguments=\"-U -DskipTests -Dsettings.security=${mss} -q\" -Dsettings.security=${mss} release:clean release:prepare --batch-mode " +
                                    "-DdevelopmentVersion=${nextVersion} -DreleaseVersion=${releaseVersion} -Dtag=kka-${releaseVersion} -e"
                            sh "mvn -Dsettings.security=${mss} -Darguments=\"-U -DskipTests -DargLine=-DdbUserSuffix=BLD -Dsettings.security=${mss}\" release:perform --batch-mode -e"
                            sh "git status"
                            sh "git checkout kka-${releaseVersion}"
                        }
                    }
					
					executeStage('Determine NEXUS_VERSION', branch, stageResult, recipients) {
                        // Находим NEXUS_VERSION
                        def pomModel = readMavenPom()
                        String versionPom = pomModel.getVersion()
                        String releaseNum = String.format('%03d', Integer.parseInt(versionPom.split('\\.')[1]))
                        String buildNum = versionPom.split('-')[1][1..-1]
                        String subversion = versionPom.split('\\.')[2][0..-6]
                        NEXUS_VERSION = "D-01.${releaseNum}.0${subversion}-D-01.${releaseNum}.0${subversion}-${buildNum}"
                        println("versionPom: ${versionPom}, releaseNum: ${releaseNum}, buildNum: ${buildNum}, subversion: ${subversion}, NEXUS_VERSION: ${NEXUS_VERSION}")
                    }

                    executeStage('Clean Package and UnitTests', branch, stageResult, recipients) {
                        // Переходим на выбранную ветку
                        sshagent([JenkinsCredentialsId]) {
							def pomModel = readMavenPom()
							String versionPom = pomModel.getVersion()
							println("versionPom: ${versionPom}")//Version control
                            String mss = pwd() + '/infrastructure/kka-deploy/mvn_ss.xml'
                            sh "mvn clean package -Dsettings.security=${mss} ${'true'.equalsIgnoreCase(skip_Tests) ? '-DskipTests' : ''} -U -q"
                        }
                    }

					// Сонар анализ
                    if ('true'.equalsIgnoreCase(need_SONAR)) {
                        executeStage('Sonar Analize', branch, stageResult, recipients) {
                            String fileContent = readFile encoding: 'UTF-8', file: "${MavenSettingsSecurityFile}"
                            String mss = pwd() + '/infrastructure/kka-deploy/mvn_ss.xml'
                            writeFile file: mss, text: fileContent
                            sh 'mvn -version'
                            withSonarQubeEnv('SonarQube67') {
                                sh "mvn sonar:sonar -q -Dsettings.security=${mss} "
                            }

                            def qg = waitForQualityGate()
                            println("Статус прохождения Sonar-анализа: ${qg.status}")
                            if (qg.status == "OK") {
                                setFlag(NEXUS_ARTIFACT, NEXUS_VERSION, "ci", "ok")
                            } else {
                                setFlag(NEXUS_ARTIFACT, NEXUS_VERSION, "ci", "err")
                            }
                        }
                    }					

                }
            }
        }
    }
}


node('clearAgent') {

    cleanWs() // Очистка рабочего пространства

    wrap([$class: 'TimestamperBuildWrapper']) {
        wrap([$class: 'AnsiColorBuildWrapper']) {

            executeStage('Checkout in clearAgent', branch, stageResult, recipients) { // Чекаут
                scmVars = checkout(
                        [$class                           : 'GitSCM',
                         branches                         : [[name: "${GitBranch}"]],
                         browser                          : [$class: 'BitbucketWeb', repoUrl: 'https://stash.delta.sbrf.ru/projects/KKA/repos/kka'],
                         doGenerateSubmoduleConfigurations: false,
                         extensions                       : [[$class: 'CleanCheckout'], [$class: 'LocalBranch', localBranch: branch]],
                         gitTool                          : 'Default',
                         submoduleCfg                     : [],
                         userRemoteConfigs                : [[credentialsId: JenkinsCredentialsId, url: project_git_url]]]
                )
            }

            withMaven(jdk: jdkName, maven: mavenVertsion, mavenSettingsConfig: mavenSettingsConfig) {
                withCredentials([file(credentialsId: mavenSettingsSecurity, variable: 'MavenSettingsSecurityFile')]) {

                    executeStage('Build Distrib', branch, stageResult, recipients) {
                        // Переходим на выбранную ветку
                        sh "git status"
                        sh "git checkout kka-${releaseVersion}"
                        String fileContent = readFile encoding: 'UTF-8', file: "${MavenSettingsSecurityFile}"
                        println "======================\n${fileContent}"
                        String mss = pwd() + '/infrastructure/kka-deploy/mvn_ss.xml'
                        println "mss: ${mss}"
                        writeFile file: mss, text: fileContent
                        dir('infrastructure/kka-deploy') {
                            def libs = ['../tools/groovy-all-2.4.12.jar', '../tools/ant-1.10.1.jar', '../tools/ant-launcher-1.10.1.jar']
                            sh "java -cp ${libs.join(':')} groovy.ui.GroovyMain build_all2.groovy ${mss ?: ''}"
                        }
                        archiveArtifacts 'infrastructure/kka-deploy/build/kka-*.zip'
                    }

                    executeStage('Nexus Upload', branch, stageResult, recipients) {
                        // Загрузка билда в нексус
                        def kkaDistribFile = findFiles(glob: 'infrastructure/kka-deploy/build/kka-*.zip')[0]
                        println("kkaDistribFile name: ${kkaDistribFile.name}, path: ${kkaDistribFile.path}, " +
                                "directory: ${kkaDistribFile.directory}, length: ${kkaDistribFile.length}")
                        withMaven(jdk: jdkName, maven: 'Maven 3.5.4', mavenSettingsConfig: 'KKA-repo') {
                            withCredentials([usernamePassword(credentialsId: 'nexusAccount', usernameVariable: 'nexusUser', passwordVariable: 'nexusPwd')]) {
                                echo "Deploy NEXUS_VERSION: ${NEXUS_VERSION}"
                                sh "mvn deploy:deploy-file -DgroupId=Nexus_PROD -DgeneratePom=true -DartifactId=${NEXUS_ARTIFACT} -Dversion=${NEXUS_VERSION} " +
                                        "-Dpackaging=zip -Dfile=${kkaDistribFile.path} -DrepositoryId=sbrf-repo-releases -Durl=https://sbrf-nexus.ca.sbrf.ru/nexus/content/repositories/Nexus_PROD " +
                                        "-Drepo.password=${nexusPwd} -Drepo.username=${nexusUser} -Dclassifier=distrib"
                            }
                        }
                        distribNexusLink = 'https://sbrf-nexus.ca.sbrf.ru/nexus/content/repositories/Nexus_PROD/Nexus_PROD/' + NEXUS_ARTIFACT + '/' + NEXUS_VERSION +
                                '/' + NEXUS_ARTIFACT + '-' + NEXUS_VERSION + '-distrib.zip'
                    }
					
					
                    if ('true'.equalsIgnoreCase(need_SAST)) {
                        executeStage('Start SAST and OSS Scan', branch, stageResult, recipients) {
                            library identifier: 'ru.sbrf.devsecops@master',
                                    retriever: legacySCM([$class                           : 'GitSCM', branches: [[name: 'master']],
                                                          doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [],
                                                          userRemoteConfigs                : [[credentialsId: JenkinsCredentialsId, url: 'ssh://git@stash.ca.sbrf.ru:7999/otib/devsecops_pipeline.git ']]])
                            def config = readYaml(file: 'infrastructure/pipeline/devsecops-config.yml')
                            commitHash = scmVars.GIT_COMMIT

                            //запуск SAST-сканирования
                            def QGstatusSast = runSastCx(config, '', branch, commitHash)
                            println("SAST_RUN:${QGstatusSast.SAST_RUN}")

                            //запуск OSS-сканирования
                            OSS_FLAG_STATUS = "none"
                            println("Запуск проверки OSS для комита ${commitHash}")
                            def QGstatusOss = runOSS(config, '', branch, commitHash)
                            println("OSS_RUN:${QGstatusOss.OSS_RUN} OSS_PASS:${QGstatusOss.OSS_PASS} OSS_HIGH_PASS:${QGstatusOss.OSS_HIGH_PASS} OSS_MEDIUM_PASS:${QGstatusOss.OSS_MEDIUM_PASS}")
                            if (!QGstatusOss.OSS_RUN) {
                                error('Сканирование OSS невозможно запустить. Нужно обратится к разработчикам ru.sbrf.devsecops')
                            }
                            if (!QGstatusOss.OSS_MEDIUM_PASS) {
                                println('Сканирование OSS завершено в штатном порядке. Обнаружено превышение по дефектам со средней критичностью')
                            }
                            if (!QGstatusOss.OSS_HIGH_PASS) {
                                println('Сканирование OSS завершено в штатном порядке. Обнаружено превышение по дефектам с высокой критичностью')
                            }
                            if (!QGstatusOss.OSS_PASS) {
                                error("Комит ${commitHash} не прошел OSS QG. Выход на CDL запрещен. Выход на CDP запрещен.")
                            } else {
                                OSS_FLAG_STATUS = "ok"
                                println('Сканирование OSS завершено успешно')
                            }
                        }
                    }            

                    if ('true'.equalsIgnoreCase(need_SAST)) {
                        executeStage('Upload SAST flags to QGM', branch, stageResult, recipients) {
                            //загрузка флага об успешном прохождении OSS-сканирования
                            if (OSS_FLAG_STATUS != "none") {
                                setFlag(NEXUS_ARTIFACT, NEXUS_VERSION, "oss", "ok")
                            }
                            //Кроме этого можно установить параметр wait_qg
                            timeout(time: 1, unit: 'HOURS') {
                                script {
                                    def ready = false;
                                    while (!ready) {
                                        def QGstatus = getQGStatus(commitHash)
                                        println("status:${QGstatus.status} qgAvailable:${QGstatus.qgAvailable}")
                                        if (QGstatus.status == "no_hash") {
                                            setFlag(NEXUS_ARTIFACT, NEXUS_VERSION, "sast", "err")
                                            error("В БД SALM отсутствует информация о cборкe ${commitHash}. Необходимо проверить корректность переданного хэша сборки и настройки DevOps Pipeline в части SAST. Сборка не готова к CDP и CDL. Выставляется флаг sast_err")
                                        }
                                        if (QGstatus.status == "failed") {
                                            setFlag(NEXUS_ARTIFACT, NEXUS_VERSION, "sast", "err")
                                        }
                                        if (QGstatus.status == "success") {
                                            ready = true
                                            def QGstatus_hash = getQGFlag(commitHash)
                                            println("SAST_RUN:${QGstatus_hash.SAST_RUN} SAST_PASS:${QGstatus_hash.SAST_PASS} no_critical_defects:${QGstatus_hash.no_critical_defects} current_hash:${QGstatus_hash.current_hash}")
                                            if (QGstatus_hash.current_hash) //Есть результаты текущего сканирования
                                            {
                                                if (QGstatus_hash.SAST_PASS) {
                                                    println("Сборка ${commitHash} прошла SAST успешно и готова к CDP. Выставляется флаг sast_ok")
                                                    setFlag(NEXUS_ARTIFACT, NEXUS_VERSION, "sast", "ok")
                                                } else {
                                                    println("Сборка ${commitHash} прошла SAST НЕ успешно. Выставляется флаг sast_err")
                                                    setFlag(NEXUS_ARTIFACT, NEXUS_VERSION, "sast", "err")
                                                }
                                            } else {
                                                error("Флаг QGstatus.status=success, при этом QGstatus_hash.current_hash==false.")
                                            }
                                        }
                                        if (QGstatus.status == "in_progress") {
                                            println("Результат анализа сборки ${commitHash} еще не готов.")
                                            sleep(600)
                                        }
                                    }
                                }
                            }
                        }
                    }

					

                    //Загрузка ReleaseNotes в QGM
                    if ('true'.equalsIgnoreCase(upload_RN)) {
                        executeStage('Upload ReleaseNotes', branch, stageResult, recipients) {
                            prev_release = sh(
                                    script: 'git branch --list *origin/release* -a | grep ' + "${branch}" + ' -B 1 | head -n 1',
                                    returnStdout: true
                            ).trim()
                            println("Хэш-комит предыдущего релиза: ${prev_release}")
                            rn = releaseNotes()
                                    .setGroup("Nexus_PROD")
                                    .setArtifact("${NEXUS_ARTIFACT}")
                                    .setVersion("${NEXUS_VERSION}")
                                    .addRepo(
                                            ".",                        //Указываем папку, в которую выкачан дистрибутив
                                            "${JenkinsCredentialsId}",      //Указываем Jenkins Credentionals ID, под которым выкачивается дистрибутив
                                            "${project_git_url}",           //Указвыаем ссылку на git
                                            true,                           //Признак основного репозитория
                                            prev_release,                   //Ссылка на коммит, начиная с которого надо искать задачи в jira
                                            commitHash,
                                            /(KKA)-[0-9]+/)                      //Ссылка на текущий коммит
                                    .setNexusUrl("https://sbrf-nexus.ca.sbrf.ru/nexus")
                                    .setArtifactRepository("Nexus_PROD")
                                    .setNexusCredsId("nexusAccount")
                                    .createAndUploadReleaseNotes()
                        }
                    }

                    notifyNexusUpload(branch, distribNexusLink, recipients)

                    // Запуск следующего Job по установке на DEV стенд
                    if ('true'.equalsIgnoreCase(dev_deploy)) {
                        executeStage('Deployment DEV Barier', branch, stageResult, recipients) {
                            build(
                                    job: 'install_kka_dev',
                                    wait: false,
                                    parameters: [
                                            string(name: 'nexus_url', value: "https://sbrf-nexus.ca.sbrf.ru/nexus/service/local/repositories/Nexus_PROD/" +
                                                    "content/Nexus_PROD/${NEXUS_ARTIFACT}/${NEXUS_VERSION}/${NEXUS_ARTIFACT}-${NEXUS_VERSION}-distrib.zip")
                                    ])
                        }
                    }
                }
            }
        }
    }
}

notifyBuild('SUCCESS', branch, stageResult, recipients) // Отправка письма об окончании установки со статусами этапов установки

def setFlag(nexusArtifact, nexusVersion, flag, status) {
    qg = qualityGates()
            .setGroup("Nexus_PROD")
            .setArtifact("${nexusArtifact}")
            .setVersion("${nexusVersion}")
            .setNexusUrl("https://sbrf-nexus.ca.sbrf.ru/nexus")
            .setArtifactRepository("Nexus_PROD")
            .setNexusCredsId("nexusAccount")
            .uploadFlag(flag, status)
}


// Отправляем письма о начале сборки
def notifyStartBuild(branch, recipients) {
    def subject = 'Начало сборки дистрибутива АС ККА: ' + branch
    def body = '<br>Время начала: ' + new Date().format('HH:mm:ss') + '<br>' + 'Ветка: ' + branch + '<br>' + 'Запустил: ' + getBuildUser() + '<br><br>' + env.BUILD_URL + 'console'
    emailext body: body, mimeType: 'text/html', subject: subject, to: recipients
}

// Получения логина пользователя, запустившего сборку
def getBuildUser() {
    return currentBuild.rawBuild.getCause(Cause.UserIdCause).getUserId()
}

// Установка статуса при удачном выполнении этапа установки
def stageSuccess(stageName, stageResult) {
    def stageStatus = 'SUCCESS'
    stageResult.put(stageName, stageStatus)
}

//Установка статуса и отправки уведомлений при неудачном выполнении этапа установки
def stageFailure(stageName, branch, stageResult, recipients) {
    def stageStatus = 'FAILURE'
    currentBuild.result = stageStatus
    stageResult.put(stageName, stageStatus)
    notifyBuild(stageStatus, branch, stageResult, recipients)
}

// Отправка письма об окончании установки со статусами этапов установки
def notifyBuild(buildStatus, branch, stageResult = [:], recipients) {
    stage_result = 'Статус установки ' + ': ' + getColor(buildStatus) + '<br><b>Этапы сборки дистрибутива АС ККА: ' + branch + ':</b><br><table cols=\'2\' border=\'0\'><tr><th style=\'width:85%\'></th><th></th></tr>'
    for (item in stageResult.keySet()) {
        stage_result = stage_result + '<tr><td>' + item + '</td><td>' + getColor(stageResult[item]) + '</td></tr>'
    }
    stage_result += '</table>' + '<br>' + env.BUILD_URL + 'console'
    def subject = 'Сборка дистрибутива АС ККА: ' + branch + ' завершена'
    def body = stage_result
    emailext body: body, mimeType: 'text/html', subject: subject, to: recipients
}

def notifyNexusUpload(branch, distrib, recipients) {
    def subject = 'Загрузка ' + branch + ' в Nexus'
    def body = 'Дистрибутив ' + branch + ' загружен в Nexus. Ссылка: ' + distrib
    emailext body: body, mimeType: 'text/html', subject: subject, to: recipients
}

// Раскраска статусов установки
def getColor(res) {
    switch (res) {
        case 'SUCCESS': return '<font color=\'green\'>' + res + '</font>'
        case 'FAILURE': return '<font color=\'red\'>' + res + '</font>'
        default: return '<font color=\'black\'>' + res + '</font>'
    }
}

def executeStage(stageName, branch, stageResult, recipients, Closure task) {
    try {
        stage(stageName, task)
        stageSuccess(stageName, stageResult) // складываем статус выполнения шага в массив "stageResult"
    } catch (e) {
        stageFailure(stageName, branch, stageResult, recipients) // складываем статус выполнения шага в массив "stageResult"
        throw e
    }
}
