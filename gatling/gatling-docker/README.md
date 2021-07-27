gatling-maven-plugin-demo
=========================

Simple showcase of a maven project using the gatling-maven-plugin.

To test it out, simply execute the following command:

Navigate to gatling-docker directory: 

    $mvn gatling:test -Dgatling.simulationClass=computerdatabase.BasicSimulation

or simply:

    $mvn gatling:test

You can download a copy of Gatling Charts Highcharts Bundle [here](https://mvnrepository.com/artifact/io.gatling.highcharts/gatling-charts-highcharts-bundle/3.6.1)

ES test on K8s pod:
* export SCENARIO_REPEAT_COUNT=10
* export VIRTUAL_USERS=1
* export GATLING_SCENARIO=com.imanage.stratus.elasticsearch.performance.query.SingleTermEsQuery
* export DATA_FILE=words-mixedlen.csv (optional)

ES test on Dev VM docker container:
* export LIBRARY_ID=888
* export CUSTOMER_ID=516
* export ES_BASE_URL="https://internal-atldev3.imanagelabs.com:9953"
* export POD_NAME=dev3pod2
* export ES_USER=workapi
* export ES_SECRET="G//RyIQoZX0z6N8Q1UATQWrnEN+IOa2/qRcpWXYH"
* export SCENARIO_REPEAT_COUNT=10
* export VIRTUAL_USERS=1
* export GATLING_SCENARIO=com.imanage.stratus.elasticsearch.performance.query.SingleTermEsQuery
* export DATA_FILE=words-mixedlen.csv (optional)


Navigate to /gatling-charts-highcharts-bundle-3.6.1/scripts and run-es-test.sh file.

To debug the test cases edit logback-test.xml file:
* To log ES query request and response
> <logger name="io.gatling.http.engine.response" level="TRACE"/>
* To log no. of hits only
> <logger name="com.imanage.stratus" level="DEBUG"/>
