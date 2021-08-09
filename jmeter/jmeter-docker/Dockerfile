FROM registry.imanage.com/m-java11-focal:latest

ENV JMETER_TEST="supervised-import-with-delete" HEAP="-XX:MaxRAMPercentage=75.0 -XX:MaxMetaspaceSize=256m" JMETER_OPTS=""

ARG JMETER_VERSION=5.4.1
ADD target/apache-jmeter-${JMETER_VERSION}.tgz /opt

COPY entrypoint.sh /usr/local/bin/entrypoint.sh
COPY test-plans /usr/local/jmeter/test-plans/
COPY jmeter.properties /usr/local/jmeter/jmeter.properties

RUN apt-get update && \
    apt-get install -y curl jq && \
    ln -s /opt/apache-jmeter-${JMETER_VERSION}/bin/jmeter /usr/bin

COPY log4j2.xml /opt/apache-jmeter-${JMETER_VERSION}/bin/log4j2.xml

WORKDIR /opt/apache-jmeter-${JMETER_VERSION}

ENTRYPOINT [ "entrypoint.sh" ]
