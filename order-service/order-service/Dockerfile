FROM jenkins/jenkins:lts
# Install plugins using Jenkins Plugin CLI
RUN jenkins-plugin-cli --plugins \
    workflow-aggregator \
    git \
    credentials-binding \
    blueocean