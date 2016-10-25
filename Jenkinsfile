node {
  def mvn = "/opt/apache-maven/bin/mvn"

  stage ("Update Version") {
    sh "${mvn} versions:set -DnewVersion=1.0-${currentBuild.number}"
  }

  stage ("Build") {
    sh "${mvn} org.jacoco:jacoco-maven-plugin:prepare-agent install"
  }
}
