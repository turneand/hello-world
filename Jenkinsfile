node {
    echo "BEFORE: JH ${env.JAVA_HOME}"
    echo "BEFORE: P ${PATH}"

  withEnv(["PATH+MAVEN=${tool 'apache-maven-3'}/bin"]) {
    env.JAVA_HOME = tool 'JDK-1.8'

    echo "AFTER: JH ${env.JAVA_HOME}"
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

    stage('SonarQube analysis') {
//      withSonarQubeEnv('SonarQube Scanner 2.8') {
      withSonarQubeEnv('sonar-server') {
        // requires SonarQube Scanner for Maven 3.2+
//        sh "mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar"
        sh "mvn -B org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar"
      }
    }
  }
}
