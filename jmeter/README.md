# jmeter-docker

Run your jmeter scripts inside of a Docker container.

After running `make`, you can run your Docker image locally with
```shell
docker run --name jmeter -it -e JMETER_TEST="supervised-import-with-delete" -e JMETER_OPTS="-Dnumber_of_threads=1 -Dramp_up_time_seconds=0 -Dtest_duration_seconds=30" --rm jmeter-docker
```

or in Kubernetes with
```shell
# make sure to adjust the image version here and in the Kubernetes yaml manifest

docker tag jmeter-docker registry.imanage.com/unverified/scale/jmeter-docker:0.0.1
docker push registry.imanage.com/unverified/scale/jmeter-docker:0.0.1

kubectl apply -f jmeter-pod.yaml
```

## Copy jmeter report from Docker or Kubernetes to your local machine
```shell
mkdir -p ./out

docker cp jmeter:/usr/local/jmeter/reports ./out/reports

kubectl cp atldev4-jmeter-test-pod:/usr/local/jmeter/reports ./out/reports
```
