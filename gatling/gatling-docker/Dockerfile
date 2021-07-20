FROM registry.imanage.com/m-java11:latest
ENTRYPOINT /bin/bash

ENV JAVA_OPTS="" LOG_LEVEL=DEBUG ROOT_LOG_LEVEL=WARN RUN_DESCRIPTION=""

ARG GATLING_VERSION=3.6.1
ENV GATLING_VERSION=3.6.1

ADD https://artifacts.imanage.com/artifactory/libs-release/com/imanage/atlas/vault-scripts/1.1.25/vault-scripts-1.1.25.tar.gz /tmp

ADD target/gatling-charts-highcharts-bundle-${GATLING_VERSION}-bundle.zip /
ADD src/test/scripts /gatling-charts-highcharts-bundle-${GATLING_VERSION}/scripts
ADD target/dependency /gatling-charts-highcharts-bundle-${GATLING_VERSION}/lib

RUN apt-get update && \
    apt-get install -y unzip jq curl nano && \
    unzip -u /gatling-charts-highcharts-bundle-${GATLING_VERSION}-bundle.zip && \
    rm -rf /gatling-charts-highcharts-bundle-${GATLING_VERSION}/user-files/simulations/* \
           /gatling-charts-highcharts-bundle-${GATLING_VERSION}/user-files/resources/* && \
    rm /gatling-charts-highcharts-bundle-${GATLING_VERSION}/conf/logback.xml && \
    mkdir -p /vault-scripts && tar -C /vault-scripts -xzvf /tmp/vault-scripts-1.1.25.tar.gz

ADD src/test/scala /gatling-charts-highcharts-bundle-${GATLING_VERSION}/user-files/simulations
ADD src/test/resources /gatling-charts-highcharts-bundle-${GATLING_VERSION}/user-files/resources
ADD src/test/resources/logback-test.xml /gatling-charts-highcharts-bundle-${GATLING_VERSION}/conf/logback.xml

ENTRYPOINT /gatling-charts-highcharts-bundle-${GATLING_VERSION}/scripts/entrypoint.sh
