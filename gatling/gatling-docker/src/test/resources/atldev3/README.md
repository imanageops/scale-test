To run gatling docker as singularity job:
* copy folder es-load-test to src/main/docker/deploy-files directory
* build and push gatling docker image to repository
* docker run --rm --name deploy m-deployer --service=es-load-test  --force-deploy --no-confirm --env=atldev3  --image=<image location>
> docker run --rm --name deploy m-deployer --service=es-load-test  --force-deploy --no-confirm --env=atldev3  --image=registry.imanage.com/unverified/ssaxena/gatling-docker:1.0.79

* Find the host node where job is running from singularity ui
* ssh in to the box and ssh in to the docker and run gatling scenarios
* Script run-es-test-scenarios.sh has few predefined scenarios

To run tests once exec in to the docker container:

export LIBRARY_ID=888
export CUSTOMER_ID=516
export ES_BASE_URL="https://internal-atldev3.imanagelabs.com:9953"
export POD_NAME=dev3pod2
export ES_USER=workapi
export ES_SECRET=<es password>
export SCENARIO_REPEAT_COUNT=2
export VIRTUAL_USERS=1
export GATLING_SCENARIO=com.imanage.stratus.elasticsearch.performance.query.SingleTermEsQuery
export DATA_FILE=words-mixedlen.csv (optional)
Navigate to /gatling-charts-highcharts-bundle-3.6.1/scripts and run-es-test.sh file.
