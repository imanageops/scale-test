#!/usr/bin/env bash
export GATLING_SCENARIO=com.imanage.stratus.elasticsearch.performance.query.${1}
export SIMULATION_DURATION=10
for vu in 1 10 20 30 40 50
do
  echo "---------------------------------------------START-----------------------------------------------"
  export VIRTUAL_USERS=$vu
  export SCENARIO_REPEAT_COUNT=1000
  ./gatling-charts-highcharts-bundle-3.6.1/scripts/run-es-test.sh
  echo "----------------------------------------------END------------------------------------------------"
done
