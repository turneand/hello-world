node {
  withEnv(["env.JAVA_HOME=${tool 'JDK-1.8'}]", "PATH+MAVEN=${tool 'apache-maven-3'}/bin", "PATH+NODE=${tool 'nodejs-7'}/bin"]) {
    echo "AFTER: JH ${env.JAVA_HOME}"
    echo "AFTER: P ${PATH}"
    echo "AFTER: OPTS ${env.MAVEN_OPTS}"

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

    stage('SonarQube analysis') {
      withSonarQubeEnv('sonar-server') {
        sh "mvn -B org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar"
      }
    }
  }
}
