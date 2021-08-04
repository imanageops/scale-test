#!/usr/bin/env bash
export GATLING_SCENARIO=com.imanage.stratus.elasticsearch.performance.query.${1}
export SIMULATION_DURATION=10
echo "---------------------------------------------START-----------------------------------------------"
export VIRTUAL_USERS=10
export SCENARIO_REPEAT_COUNT=1000
./gatling-charts-highcharts-bundle-3.6.1/scripts/run-es-test.sh
echo "----------------------------------------------END------------------------------------------------"
echo "---------------------------------------------START-----------------------------------------------"
export VIRTUAL_USERS=20
export SCENARIO_REPEAT_COUNT=1000
./gatling-charts-highcharts-bundle-3.6.1/scripts/run-es-test.sh
echo "----------------------------------------------END------------------------------------------------"
echo "---------------------------------------------START-----------------------------------------------"
export VIRTUAL_USERS=30
export SCENARIO_REPEAT_COUNT=1000
./gatling-charts-highcharts-bundle-3.6.1/scripts/run-es-test.sh
echo "----------------------------------------------END------------------------------------------------"
echo "---------------------------------------------START-----------------------------------------------"
export VIRTUAL_USERS=40
export SCENARIO_REPEAT_COUNT=1000
./gatling-charts-highcharts-bundle-3.6.1/scripts/run-es-test.sh
echo "----------------------------------------------END------------------------------------------------"
echo "---------------------------------------------START-----------------------------------------------"
export VIRTUAL_USERS=50
export SCENARIO_REPEAT_COUNT=1000
./gatling-charts-highcharts-bundle-3.6.1/scripts/run-es-test.sh
echo "----------------------------------------------END------------------------------------------------"
