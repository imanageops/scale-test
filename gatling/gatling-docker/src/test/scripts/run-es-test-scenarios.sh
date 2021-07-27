#!/usr/bin/env bash
export GATLING_SCENARIO=com.imanage.stratus.elasticsearch.performance.query.${1}
echo "---------------------------------------------START-----------------------------------------------"
export VIRTUAL_USERS=1
export SCENARIO_REPEAT_COUNT=1000
./gatling-charts-highcharts-bundle-3.6.1/scripts/run-es-test.sh
echo "----------------------------------------------END------------------------------------------------"
echo "---------------------------------------------START-----------------------------------------------"
export VIRTUAL_USERS=40
export SCENARIO_REPEAT_COUNT=25
./gatling-charts-highcharts-bundle-3.6.1/scripts/run-es-test.sh
echo "----------------------------------------------END------------------------------------------------"
echo "---------------------------------------------START-----------------------------------------------"
export VIRTUAL_USERS=40
export SCENARIO_REPEAT_COUNT=250
./gatling-charts-highcharts-bundle-3.6.1/scripts/run-es-test.sh
echo "----------------------------------------------END------------------------------------------------"
echo "---------------------------------------------START-----------------------------------------------"
export VIRTUAL_USERS=40
export SCENARIO_REPEAT_COUNT=2500
./gatling-charts-highcharts-bundle-3.6.1/scripts/run-es-test.sh
echo "----------------------------------------------END------------------------------------------------"
echo "---------------------------------------------START-----------------------------------------------"
export VIRTUAL_USERS=40
export SCENARIO_REPEAT_COUNT=10000
./gatling-charts-highcharts-bundle-3.6.1/scripts/run-es-test.sh
echo "----------------------------------------------END------------------------------------------------"
