node {
  echo "BEFORE: JAVA_HOME ${env.JAVA_HOME}"
  echo "BEFORE: P ${PATH}"

  withEnv(["env.JAVA_HOME=${tool 'jdk-1.8'}]"]) {
    echo "AFTER: JAVA_HOME ${env.JAVA_HOME}"
    echo "AFTER: P ${PATH}"

    stage ("Checkout") {
      checkout scm
    }

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

    withEnv(["PATH+GRADLE=${tool 'gradle-3'}/bin","ANDROID_HOME=/opt/android-sdk-linux"]) {
      stage ("Gradle") {
        sh "gradle -version" 
      }

      stage ("Gradle") {
        sh "gradle -b android/build.gradle build" 
      }
    }
  }
}
