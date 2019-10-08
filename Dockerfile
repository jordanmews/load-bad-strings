# This builds the test repo and downloads all maven dependencies.

# EXAMPLE USAGE
# build image, create container, start and run
# docker build -t mvngatimage . --no-cache
# docker run -it --name mvngatcontainer mvngatimage java -cp target/string-tests-1.0-SNAPSHOT.jar io.gatling.app.Gatling -s config.testSim

FROM maven:3.5-jdk-8

ENV TESTS="tests"

# Debian Jessie mirrors were removed
RUN echo "deb http://archive.debian.org/debian jessie-backports main" > /etc/apt/sources.list.d/jessie-backports.list
RUN sed -i '/deb http:\/\/deb.debian.org\/debian jessie-updates main/d' /etc/apt/sources.list

# Debian Jessie mirrors were removed
# Jessie's apt doesn't support [check-valid-until=no] so we have to use this instead
RUN echo "Acquire::Check-Valid-Until false;" |  tee -a /etc/apt/apt.conf.d/10-nocheckvalid

RUN apt-get update && apt-get install -y curl
RUN curl -sL https://deb.nodesource.com/setup_11.x | bash -
RUN apt-get update && apt-get install -y nodejs

RUN mkdir -p ${TESTS}/src
RUN chown nobody:nogroup ${TESTS}

# This special hack is so we can use the scala-maven plugin to package a nice jar into the container and have dependencies baked into the image.
# But, the gatling plugins assume that your simulations will run in the test directory.  This causes errors on running the engine locally.
# I tried all sorts of their documented overrides in the pom.xml, gatling.conf and my own hackery modifying the Engine and IDEPathHelper class, but none worked.
# Maybe I just missed something, but done with this for now.
COPY helpers ${TESTS}/helpers
COPY src/test ${TESTS}/src/main

COPY settings.xml /usr/share/maven/ref/
COPY pom.xml ${TESTS}/

WORKDIR ${TESTS}

RUN mvn -Duser.home=/tmp clean install && rm -rf /tmp/.m2
