.PHONY: all clean package deploy gatling-docker

GATLING_VERSION=3.6.0

all: download package gatling-docker

clean:

package:
	mvn package dependency:copy-dependencies

deploy:

gatling-docker:
	docker build -t gatling-docker --build-arg GATLING_VERSION=$(GATLING_VERSION) .

download:
ifeq ($(wildcard ./target/gatling-charts-highcharts-bundle-$(GATLING_VERSION)-bundle.zip),)
	mkdir -p target
	@echo "Did not find gatling-charts-highcharts-bundle-$(GATLING_VERSION)-bundle.zip. Downloading..."
	$() curl -Lo "target/gatling-charts-highcharts-bundle-$(GATLING_VERSION)-bundle.zip" "https://repo1.maven.org/maven2/io/gatling/highcharts/gatling-charts-highcharts-bundle/$(GATLING_VERSION)/gatling-charts-highcharts-bundle-$(GATLING_VERSION).zip"
endif
