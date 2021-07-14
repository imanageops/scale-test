#!/usr/bin/env bash

# watch kubectl top output every ${SLEEP_TIME} seconds and output to a file based on a label that you pass in,
# e.g. app=atldev4-locust-load-test-deployment or app.kubernetes.io/name=prizm
#
# Note that the metrics are updated by default every 60s and are controlled by the metric-resolution flag:
# https://github.com/kubernetes-sigs/metrics-server/blob/master/FAQ.md
#
# This script ignores output that has not changed since the last call to `kubectl top`

TOTAL_RUN_TIME=${1:?"Please provide a TOTAL_RUN_TIME"}
SLEEP_TIME=29

#LABEL=${2:-"app=atldev4-locust-load-test-deployment"}
LABEL=${2:?"Please provide a label, e.g. app.kubernetes.io/name=prizm"}

START_TIME=$(date +"%Y.%m.%d-%H:%M:%S")

OUTPUT_FILE=${LABEL//[\/\\=]/_}

echo "Outputting to ${OUTPUT_FILE}"

PREVIOUS_RESULT=""

for i in $(seq 1 $(((TOTAL_RUN_TIME + SLEEP_TIME - 1) / SLEEP_TIME))); do
  echo "${START_TIME} ${i}" | tee -a "${OUTPUT_FILE}"
  NEW_RESULT=$(kubectl top no --sort-by cpu -l "${LABEL}")
  if [[ "${PREVIOUS_RESULT}" != "${NEW_RESULT}" ]]; then
    echo "${NEW_RESULT}" | tee -a "${OUTPUT_FILE}"
    PREVIOUS_RESULT=${NEW_RESULT}
  fi
  sleep ${SLEEP_TIME}
done
