node {
  def mvn = "/opt/apache-maven/bin/mvn --batch-mode"

  stage ("Checkout") {
    checkout scm
  }
  
  stage ("Update Version") {
    sh "ls -l"
    sh "pwd"
    sh "${mvn} versions:set -DnewVersion=1.0-${currentBuild.number}"
  }

  stage ("Build") {
    sh "${mvn} org.jacoco:jacoco-maven-plugin:prepare-agent install"
  }

  stage('SonarQube analysis') {
    withSonarQubeEnv('DEFAULT') {
      // requires SonarQube Scanner for Maven 3.2+
      sh '${mvn} sonar:sonar'
    }
  }
}
