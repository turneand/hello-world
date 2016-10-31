node {
  withEnv(["PATH+MAVEN=${tool 'apache-maven-3.3.9'}/bin"]) {
    stage ("Checkout") {
      checkout scm
    }

    stage ("Update Version") {
      sh mvn versions:set -DnewVersion=1.0-${currentBuild.number}"
    }

    stage ("Build") {
      sh "mvn org.jacoco:jacoco-maven-plugin:prepare-agent install"
    }

    stage('SonarQube analysis') {
      withSonarQubeEnv('SonarQube Scanner 2.8') {
        // requires SonarQube Scanner for Maven 3.2+
//        sh "mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar"
        sh "mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar"
      }
    }
  }
}
