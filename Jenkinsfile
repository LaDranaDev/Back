pipeline {
  agent any
  options { timestamps(); ansiColor('xterm') }

  environment {
    APP_NAME      = 'monitoreoapi'
    IMAGE_REG     = 'registry.internal.local/monitoreo'
    IMAGE_NAME    = "${IMAGE_REG}/${APP_NAME}"
    GIT_SHORT_SHA = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
    VERSION       = "${env.BRANCH_NAME == 'main' ? '1.0.0' : '0.0.0-' + GIT_SHORT_SHA}"
  }

  stages {
    stage('Checkout') { steps { checkout scm } }

    stage('Build & Tests') {
      steps { sh 'mvn -B -U clean verify' }
      post {
        always {
          junit 'target/surefire-reports/*.xml'
          jacoco execPattern: 'target/jacoco.exec'
        }
      }
    }

    stage('SonarQube') {
      when { expression { return env.BRANCH_NAME != null } }
      environment {
        SONAR_HOST_URL = credentials('sonar-host-url')
        SONAR_TOKEN    = credentials('sonar-token')
      }
      steps {
        withSonarQubeEnv('sonar') {
          sh '''
            mvn -B -DskipTests sonar:sonar \
              -Dsonar.host.url=$SONAR_HOST_URL \
              -Dsonar.login=$SONAR_TOKEN
          '''
        }
      }
    }

    stage('Quality Gate') {
      when { expression { return env.BRANCH_NAME != null } }
      steps { timeout(time: 10, unit: 'MINUTES') { waitForQualityGate abortPipeline: true } }
    }

    stage('Fortify') {
      when { anyOf { branch 'main'; branch 'develop' } }
      steps {
        withCredentials([string(credentialsId: 'fortify-token', variable: 'FORTIFY_TOKEN')]) {
          sh '''
            sourceanalyzer -b monitoreoapi -clean || true
            sourceanalyzer -b monitoreoapi -Xmx2G -jdk 17 .
            sourceanalyzer -b monitoreoapi -scan -f target/fortify.fpr
            fortifyclient uploadFPR -file target/fortify.fpr -project monitoreoapi -version ${VERSION} \
              -url https://ssc.internal.local -authtoken ${FORTIFY_TOKEN}
          '''
        }
      }
      post { always { archiveArtifacts artifacts: 'target/fortify.fpr', fingerprint: true } }
    }

    stage('Build Image') {
      steps { sh 'docker build -t ${IMAGE_NAME}:${VERSION} -t ${IMAGE_NAME}:latest .' }
    }

    stage('Push Image') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'registry-creds', usernameVariable: 'REG_USER', passwordVariable: 'REG_PASS')]) {
          sh '''
            echo "$REG_PASS" | docker login ${IMAGE_REG} -u "$REG_USER" --password-stdin
            docker push ${IMAGE_NAME}:${VERSION}
            docker push ${IMAGE_NAME}:latest
          '''
        }
      }
    }

    stage('Deploy DEV') {
      when { anyOf { branch 'develop'; branch pattern: "feat/.*", comparator: "REGEXP" } }
      environment {
        // cred de Jenkins con kubeconfig de dev
        KUBECONFIG_CRED = 'kubeconfig-dev'
        DB_USER_CRED    = 'db-user-dev'
        DB_PASS_CRED    = 'db-pass-dev'
      }
      steps {
        withCredentials([
          file(credentialsId: env.KUBECONFIG_CRED, variable: 'KUBECONFIG'),
          string(credentialsId: env.DB_USER_CRED, variable: 'DB_USER'),
          string(credentialsId: env.DB_PASS_CRED, variable: 'DB_PASS')
        ]) {
          sh '''
            cd k8s/dev
            # namespace una vez (idempotente)
            kubectl apply -f - <<EOF
apiVersion: v1
kind: Namespace
metadata:
  name: monitoreo-dev
EOF
            kubectl apply -f configmap.yaml
            # secret a partir de credenciales del job
            kubectl -n monitoreo-dev create secret generic monitoreoapi-secrets \
              --from-literal=SPRING_DATASOURCE_USERNAME=$DB_USER \
              --from-literal=SPRING_DATASOURCE_PASSWORD=$DB_PASS \
              --dry-run=client -o yaml | kubectl apply -f -
            kubectl apply -f service.yaml
            # actualiza imagen antes de aplicar deployment (si ya existe)
            sed -i "s#registry.internal.local/monitoreo/monitoreoapi:latest#${IMAGE_NAME}:${VERSION}#g" deployment.yaml
            kubectl apply -f deployment.yaml
          '''
        }
      }
    }
  }

  post { always { cleanWs() } }
}
