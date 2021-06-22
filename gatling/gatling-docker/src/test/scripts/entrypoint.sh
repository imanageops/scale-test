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

if [[ ! "${JAVA_OPTS}" =~ .*"-DapiBaseUri=".* ]]; then
  echo "Did not find -DapiBaseUri in the configuration. Using default value."
  export JAVA_OPTS="${JAVA_OPTS} -DapiBaseUri=https://atldev1.imanagelabs.com"
fi

if [[ ! "${JAVA_OPTS}" =~ .*"-DelasticsearchBasePath=".* ]]; then
  echo "Did not find -DelasticsearchBasePath in the configuration. Using default value."
  export JAVA_OPTS="${JAVA_OPTS} -DelasticsearchBasePath=https://es7-atldev4pod2-elasticsearch.service.imanagecloud.com:9950/dm.atldev4pod2.av.046"
fi

if [[ ! "${JAVA_OPTS}" =~ .*"-DsleepTimeUntilElasticsearchQuery=".* ]]; then
  echo "Did not find -DsleepTimeUntilElasticsearchQuery in the configuration. Using default value."
  export JAVA_OPTS="${JAVA_OPTS} -DsleepTimeUntilElasticsearchQuery=30"
fi

echo "JAVA_OPTS=${JAVA_OPTS}"

echo "Starting ${GATLING_SCENARIO} scenario at $(date)"

/gatling-charts-highcharts-bundle-3.6.0/bin/gatling.sh -s ${GATLING_SCENARIO}
