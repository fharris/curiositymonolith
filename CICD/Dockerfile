FROM jenkins/jenkins:lts-jdk17
USER root
RUN apt-get update && apt-get install -y lsb-release
RUN curl -fsSLo /usr/share/keyrings/docker-archive-keyring.asc \
  https://download.docker.com/linux/debian/gpg
RUN echo "deb [arch=$(dpkg --print-architecture) \
  signed-by=/usr/share/keyrings/docker-archive-keyring.asc] \
  https://download.docker.com/linux/debian \
  $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list
RUN apt-get update && apt-get install -y docker-ce-cli
RUN curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
RUN install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl

USER jenkins
ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false
ENV CASC_JENKINS_CONFIG /var/casc_home/casc.yaml
COPY --chown=jenkins:jenkins plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt
COPY --chown=jenkins:jenkins casc.yaml /var/casc_home/casc.yaml
COPY --chown=jenkins:jenkins jenkins-cli.jar /var/jenkins_home/jenkins-cli.jar
COPY --chown=jenkins:jenkins configurecuriosity.xml /var/jenkins_home/configurecuriosity.xml
COPY --chown=jenkins:jenkins buildcuriosity.xml /var/jenkins_home/buildcuriosity.xml
COPY --chown=jenkins:jenkins deploycuriosity.xml /var/jenkins_home/deploycuriosity.xml
COPY --chown=jenkins:jenkins jenkins_credentials.xml /var/jenkins_home/jenkins_credentials.xml

COPY --chown=jenkins:jenkins import-jobs.sh /var/jenkins_home/import-jobs.sh