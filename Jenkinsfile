// Enables SDK auto-install, and uses it to run the given block
def withAndroidSdk(String sdkDir = '/tmp/android-sdk',
 Closure body) {
 // Create the SDK directory, and accept the licences
 // (see: d.android.com/r/studio-ui/export-licenses.html)
 writeFile file: "${sdkDir}/licenses/android-sdk-license",
 text: 'e6b7c2ab7fa2298c1...\n...5d1a37fbf41ea526'
 // Run the given closure with this SDK directory
 withEnv(["ANDROID_HOME=${sdkDir}"]) {
 body()
 }
}

node('android') {
  echo "BEFORE: JAVA_HOME ${env.JAVA_HOME}"
  echo "BEFORE: P ${PATH}"
/*
  docker.withTool("mydocker") {
    docker.image('maven:3.3.3-jdk-8').inside {
      checkout scm
      sh 'mvn -B versions:set -DnewVersion=1.0-${currentBuild.number}'
      sh 'mvn -B install'
    }
  }
*/

  withEnv(["env.JAVA_HOME=${tool 'jdk-1.8'}]"]) {
    echo "AFTER: JAVA_HOME ${env.JAVA_HOME}"
    echo "AFTER: P ${PATH}"

    stage ("Checkout") {
      checkout scm
    }
/*
    withEnv(["PATH+MAVEN=${tool 'apache-maven-3'}/bin"]) {
      stage ("Update Version") {
        sh "mvn -B versions:set -DnewVersion=1.0-${currentBuild.number}"
      }

      stage ("Build") {
        sh "mvn -B org.jacoco:jacoco-maven-plugin:prepare-agent install"
      }

      stage('SonarQube analysis') {
        withSonarQubeEnv('sonar-server') {
          sh "mvn -B org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar"
        }
      }
    }

    withEnv(["PATH+NODE=${tool 'nodejs-7'}/bin"]) {
      stage ("Node") {
        sh "node hello.js" 
      }
    }
*/
    
    withAndroidSdk() {
//    withEnv(["PATH+GRADLE=${tool 'gradle-3'}/bin","ANDROID_HOME=/opt/android-sdk-linux"]) {
      stage ("Gradle") {
        //sh "gradle -version" 
        //sh "gradle -b android/build.gradle build" 
        sh "./android/gradlew -version" 
        sh "./android/gradlew -b android/build.gradle build" 
      }
     
      stage('SonarQube analysis') {
        withSonarQubeEnv('sonar-server') {
        sh './android/gradlew sonarqube'
      }
    }
  }
     
    }
  }
}
