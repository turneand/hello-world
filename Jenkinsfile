node {
  echo "BEFORE: JAVA_HOME ${env.JAVA_HOME}"
  echo "BEFORE: P ${PATH}"

  withEnv(["env.JAVA_HOME=${tool 'jdk-1.8'}]", "PATH+MAVEN=${tool 'apache-maven-3'}/bin", "PATH+NODE=${tool 'nodejs-7'}/bin", "PATH+GRADLE=${tool 'gradle-3'}/bin"]) {
    echo "AFTER: JAVA_HOME ${env.JAVA_HOME}"
    echo "AFTER: P ${PATH}"

    stage ("Checkout") {
      checkout scm
    }

    stage ("Update Version") {
      sh "mvn -B versions:set -DnewVersion=1.0-${currentBuild.number}"
    }

    stage ("Build") {
      sh "mvn -B org.jacoco:jacoco-maven-plugin:prepare-agent install"
    }

    stage ("Node") {
      sh "node hello.js" 
    }

    stage ("Gradle") {
      sh "gradle -version" 
    }

    stage ("Gradle") {
      sh "gradle -f android/build.gradle" 
    }

    stage('SonarQube analysis') {
      withSonarQubeEnv('sonar-server') {
        sh "mvn -B org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar"
      }
    }
  }
}
