To run gatling docker as Singularity job:
* copy folder es-load-test-singularity to src/main/docker/deploy-files directory
* build and push gatling docker image to repository
* docker run --rm --name deploy m-deployer --service=es-load-test  --force-deploy --no-confirm --env=atldev3  --image=<image location>
> docker run --rm --name deploy m-deployer --service=es-load-test  --force-deploy --no-confirm --env=atldev3  --image=registry.imanage.com/unverified/ssaxena/gatling-docker:1.0.79

* Find the host node where job is running from Singularity ui
* ssh in to the box and ssh in to the docker and run gatling scenarios
* Script run-es-test-scenarios.sh has few predefined scenarios

To run gatling docker as Marathon Service:
* copy folder es-load-test-marathon to src/main/docker/deploy-files directory
* build and push gatling docker image to repository
* docker run --rm --name deploy m-deployer --service=es-load-test-marathon --force-deploy --no-confirm --env=atldev3 --image=<image location>
> docker run --rm --name deploy m-deployer --service=es-load-test-marathon --force-deploy --no-confirm --env=atldev3 --image=registry.imanage.com/unverified/ssaxena/gatling-load:2.0.1

* Find the host node where job is running from Marathon ui
* ssh in to the box and ssh in to the docker and run gatling scenarios
* Script run-es-test-scenarios.sh has few predefined scenarios
* Few helpful commands:
  * to clear result directory
    > rm -rf ./gatling-charts-highcharts-bundle-3.6.1/results/
  * to run different ES test scenarios
    > nohup /gatling-charts-highcharts-bundle-3.6.1/scripts/run-es-contant-duration-scenarios.sh &

To run tests exec in to the docker container and check/set below environment variables:
* export LIBRARY_ID=888
* export CUSTOMER_ID=516
* export ES_BASE_URL="https://es7-dev3pod2-elasticsearch.service.imanagecloud.com:9950"
* export POD_NAME=dev3pod2
* export ES_USER=workapi
* export ES_SECRET=`<es password>`
* export SCENARIO_REPEAT_COUNT=2
* export VIRTUAL_USERS=1
* export GATLING_SCENARIO=com.imanage.stratus.elasticsearch.performance.query.SingleTermEsQuery
* export DATA_FILE=words-mixedlen.csv (optional)
* export SIMULATION_DURATION=10 (optional)

Navigate to /gatling-charts-highcharts-bundle-3.6.1/scripts and run-es-test.sh file.
