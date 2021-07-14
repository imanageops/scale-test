#!/bin/sh

echo "--------------"
echo " JMETER TEST"
echo "--------------"

cd /usr/local/jmeter/ || exit
report_dir="reports/$(date +'%Y%m%d-%H%M')"

mkdir -p "${report_dir}"

echo "Outputting reports to ${report_dir}"

if [ "$(echo "${ENVIRONMENT}" | tr '[:upper:]' '[:lower:]')" = "atldev4" ]; then
  printf "# atldev4 entry\n10.0.74.201\tatldev1.imanagelabs.com\n" | tee -a /etc/hosts > /dev/null
fi

if ! expr "${JMETER_OPTS}" : '.*-Dnumber_of_threads=.*' > /dev/null ; then
    echo "Did not find -Dnumber_of_threads in the configuration. Using default value."
    export JMETER_OPTS="${JMETER_OPTS} -Dnumber_of_threads=10"
fi
if ! expr "${JMETER_OPTS}" : '.*-Dramp_up_time_seconds=.*' > /dev/null ; then
  echo "Did not find -Dramp_up_time_seconds in the configuration. Using default value."
  export JMETER_OPTS="${JMETER_OPTS} -Dramp_up_time_seconds=60"
fi
if ! expr "${JMETER_OPTS}" : '.*-Dtest_duration_seconds=.*' > /dev/null ; then
  echo "Did not find -Dtest_duration_seconds in the configuration. Using default value."
  export JMETER_OPTS="${JMETER_OPTS} -Dtest_duration_seconds=60"
fi
if ! expr "${JMETER_OPTS}" : '.*-Dcustomer_id=.*' > /dev/null ; then
  echo "Did not find -Dcustomer_id in the configuration. Using default value."
  export JMETER_OPTS="${JMETER_OPTS} -Dcustomer_id=516"
fi
if ! expr "${JMETER_OPTS}" : '.*-Dlibrary_name=.*' > /dev/null ; then
  echo "Did not find -Dlibrary_name in the configuration. Using default value."
  export JMETER_OPTS="${JMETER_OPTS} -Dlibrary_name=ATLDEV4POD1"
fi
if ! expr "${JMETER_OPTS}" : '.*-Dfolder_number=.*' > /dev/null ; then
  echo "Did not find -Dfolder_number in the configuration. Using default value."
  export JMETER_OPTS="${JMETER_OPTS} -Dfolder_number=9"
fi

echo "JMETER_OPTS=${JMETER_OPTS}"

# run jmeter
jmeter -n -t "test-plans/${JMETER_TEST}.jmx" -l "${report_dir}/jmeter.log" -p jmeter.properties -e -o "${report_dir}"
#jmeter -n -t conductor-ocr-work-flow-submission.jmx -p jmeter.properties -o /usr/local/jmeter/reports/${report_dir}
#jmeter -n -t conductor-ocr-work-flow-submission.jmx -p jmeter.properties -o /usr/local/jmeter/reports -l jmeter.log &
#jmeter -n -t conductor-ocr-work-flow-submission.jmx -p jmeter.properties -l jmeter.log &
# copy jmeter report on local machine
# kubectl cp atldev4-jmeter-test-pod:/usr/local/jmeter/report.jtl ./test-report-20210614-3.jtl
# generate HTML repprt using jmeter
# ~/tools/apache-jmeter-5.4.1/bin/jmeter -g test-report-20210614-3.jtl -o ./htmlreport-3
sleep 100000
