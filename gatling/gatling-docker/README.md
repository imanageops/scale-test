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

Navigate to /gatling-charts-highcharts-bundle-3.6.1/scripts and run-es-test.sh file.
