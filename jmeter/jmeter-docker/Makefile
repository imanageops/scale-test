.PHONY: all clean deploy jmeter-docker

JMETER_VERSION=5.4.1

all: download jmeter-docker

clean:

deploy:

jmeter-docker:
	docker build -t jmeter-docker --build-arg JMETER_VERSION=$(JMETER_VERSION) .

download:
ifeq ($(wildcard ./target/apache-jmeter-$(JMETER_VERSION).tgz),)
	mkdir -p target
	@echo "Did not find apache-jmeter-$(JMETER_VERSION).tgz. Downloading..."
	$() curl -Lo "target/apache-jmeter-$(JMETER_VERSION).tgz" "https://downloads.apache.org/jmeter/binaries/apache-jmeter-$(JMETER_VERSION).tgz"
endif
