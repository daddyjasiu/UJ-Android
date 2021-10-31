FROM ubuntu:20.04

# Update OS, CURL and unzip
RUN apt update && apt upgrade -y
RUN apt install curl -y
RUN apt install zip -y
RUN apt install unzip -y

RUN useradd -ms /bin/bash admin
RUN adduser admin sudo

USER admin
WORKDIR /home/admin/

RUN curl -s "https://get.sdkman.io/" | bash
RUN chmod a+x "$HOME/.sdkman/bin/sdkman-init.sh"

# Install SDKMAN
RUN /bin/bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install java 11.0.13.8.1-amzn"
RUN /bin/bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install gradle 7.2"
RUN /bin/bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install kotlin 1.5.31"

ENV PATH=/home/admin/.sdkman/candidates/java/current/bin:$PATH
ENV PATH=/home/admin/.sdkman/candidates/gradle/current/bin:$PATH
ENV PATH=/home/admin/.sdkman/candidates/kotlin/current/bin:$PATH