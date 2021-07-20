#!/usr/bin/env bash

echo "GATLING_SCENARIO: ${GATLING_SCENARIO}"
echo "LIBRARY_ID: ${LIBRARY_ID}"
echo "CUSTOMER_ID: ${CUSTOMER_ID}"
echo "POD_NAME: ${POD_NAME}"
echo "ES_BASE_URL: ${ES_BASE_URL}"

if [[ -z ${GATLING_SCENARIO} ]]; then
  echo -e "Please define a Gatling scenario to run."
  exit 1
fi
if [[ -z ${LIBRARY_ID} ]]; then
  echo -e "Please provide LIBRARY_ID."
  exit 1
fi
if [[ -z ${CUSTOMER_ID} ]]; then
  echo -e "Please provide CUSTOMER_ID."
  exit 1
fi
if [[ -z ${POD_NAME} ]]; then
  echo -e "Please provide POD_NAME."
  exit 1
fi
if [[ -z ${ES_BASE_URL} ]]; then
  echo -e "Please provide ES_BASE_URL."
  exit 1
fi

# elasticsearch api_credentials Credentials
account_json=$(/vault-scripts/get-vault-secret "atldev4pod1-elasticsearch/api_credentials") || exit 1

es_account=$(echo "${account_json}" | jq -e -r '.username') || {
  echo "ERROR: Failed to extract atldev4pod1-elasticsearch/api_credentials"
  exit 1
}

es_account_key=$(echo "${account_json}" | jq -e -r '.password') || {
  echo "ERROR: Failed to extract atldev4pod1-elasticsearch/api_credentials"
  exit 1
}

if [ -z "${es_account}" ]; then
  echo "ERROR: es_account cannot be empty"
  exit 1
fi

if [ -z "${es_account_key}" ]; then
  echo "ERROR: es_account_key cannot be empty"
  exit 1
fi

export ES_USER="${es_account}";
export ES_SECRET="${es_account_key}";

echo "JAVA_OPTS=${JAVA_OPTS}"

echo "Starting ${GATLING_SCENARIO} scenario at $(date)"

/gatling-charts-highcharts-bundle-${GATLING_VERSION}/bin/gatling.sh -s ${GATLING_SCENARIO}
