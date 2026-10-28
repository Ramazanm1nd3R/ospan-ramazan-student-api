# Jenkins controller for the Student API (Ramazan Ospan): JDK 21, Docker CLI + buildx, plugins, configuration as code
FROM jenkins/jenkins:lts
USER root
# Docker CLI only: builds run on the host Docker daemon through the mounted /var/run/docker.sock
RUN set -eux; arch="$(uname -m)"; \
    curl -fsSL "https://download.docker.com/linux/static/stable/${arch}/docker-29.3.1.tgz" \
      | tar -xz -C /usr/local/bin --strip-components=1 docker/docker; \
    bx="$(curl -fsSL https://api.github.com/repos/docker/buildx/releases/latest | grep '"tag_name"' | cut -d'"' -f4)"; \
    mkdir -p /usr/libexec/docker/cli-plugins; \
    curl -fsSL -o /usr/libexec/docker/cli-plugins/docker-buildx \
      "https://github.com/docker/buildx/releases/download/${bx}/buildx-${bx}.linux-$( [ "$arch" = aarch64 ] && echo arm64 || echo amd64 )"; \
    chmod +x /usr/libexec/docker/cli-plugins/docker-buildx; \
    docker --version; docker buildx version
USER jenkins
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt
COPY casc.yaml /usr/share/jenkins/casc.yaml
ENV CASC_JENKINS_CONFIG=/usr/share/jenkins/casc.yaml \
    JAVA_OPTS="-Djenkins.install.runSetupWizard=false"
