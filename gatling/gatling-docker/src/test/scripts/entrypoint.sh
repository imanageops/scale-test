#!/usr/bin/env bash

if [[ -z ${GATLING_SCENARIO} ]]; then
  echo -e "Please define a Gatling scenario to run."
  exit 1
fi

if [[ ! "${JAVA_OPTS}" =~ .*"-DconstantConcurrentUsers=".* ]]; then
  echo "Did not find -DconstantConcurrentUsers in the configuration. Using default value."
  export JAVA_OPTS="${JAVA_OPTS} -DconstantConcurrentUsers=1"
fi

if [[ ! "${JAVA_OPTS}" =~ .*"-DtestDurationSeconds=".* ]]; then
  echo "Did not find -DtestDurationSeconds in the configuration. Using default value."
  export JAVA_OPTS="${JAVA_OPTS} -DtestDurationSeconds=600"
fi

echo "JAVA_OPTS=${JAVA_OPTS}"

echo "Starting ${GATLING_SCENARIO} scenario at $(date)"

/gatling-charts-highcharts-bundle-${GATLING_VERSION}/bin/gatling.sh -s ${GATLING_SCENARIO}
