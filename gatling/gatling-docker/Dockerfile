FROM registry.imanage.com/m-java11:latest
ENTRYPOINT /bin/bash

ENV JAVA_OPTS="" LOG_LEVEL=DEBUG ROOT_LOG_LEVEL=WARN RUN_DESCRIPTION=""

ARG GATLING_VERSION=3.6.0

ADD target/gatling-charts-highcharts-bundle-${GATLING_VERSION}-bundle.zip /
ADD src/test/scripts /gatling-charts-highcharts-bundle-${GATLING_VERSION}/scripts
ADD target/dependency /gatling-charts-highcharts-bundle-${GATLING_VERSION}/lib

RUN apt-get update && \
    apt-get install -y unzip && \
    rm -rf /gatling-charts-highcharts-bundle-${GATLING_VERSION}/user-files/simulations/* \
           /gatling-charts-highcharts-bundle-${GATLING_VERSION}/user-files/resources/*

ADD src/test/scala /gatling-charts-highcharts-bundle-${GATLING_VERSION}/user-files/simulations
ADD src/test/resources /gatling-charts-highcharts-bundle-${GATLING_VERSION}/user-files/resources

ENTRYPOINT /gatling-charts-highcharts-bundle-${GATLING_VERSION}/scripts/entrypoint.sh
