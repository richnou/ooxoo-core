// OOXOO
node {
 
  properties([pipelineTriggers([[$class: 'GitHubPushTrigger']])])
  def mvnHome = tool 'maven3'

  stage('Clean') {
    checkout scm
    sh "${mvnHome}/bin/mvn -B clean"
  }

  stage('Build') {
    sh "${mvnHome}/bin/mvn -B  compile test-compile"
  }

  stage('Test') {
    sh "${mvnHome}/bin/mvn -B -Dmaven.test.failure.ignore test"
    junit '**/target/surefire-reports/TEST-*.xml'
  }

  if (env.BRANCH_NAME == 'dev' || env.BRANCH_NAME == 'master') {
    
    stage('Deploy') {
      sh "${mvnHome}/bin/mvn -B -DskipTests=true deploy"
      step([$class: 'ArtifactArchiver', artifacts: '**/ooxoo-core/target/*.jar', fingerprint: true])
      step([$class: 'ArtifactArchiver', artifacts: '**/maven-ooxoo-plugin/target/*.jar', fingerprint: true])
    }

    // Trigger sub builds on dev
    if (env.BRANCH_NAME == 'dev') {
      stage("Downstream") {
        build job: '../ooxoo-db/dev'
        build job: '../vui2/dev'
      }
    }

  } else {
    
    stage('Package') {
        sh "${mvnHome}/bin/mvn -B -Dmaven.test.failure.ignore package"
        step([$class: 'ArtifactArchiver', artifacts: '**/ooxoo-core/target/*.jar', fingerprint: true])
        step([$class: 'ArtifactArchiver', artifacts: '**/maven-ooxoo-plugin/target/*.jar', fingerprint: true])
    }
  
  }

  


}
