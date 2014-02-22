# Path to JBoss AS / EAP / Wildfly
JBOSS_PATH = ../wildfly-8.0.0.CR1

all: run

compile:
	mvn -Pdev -Dmaven.test.skip=true install

install: compile
	cp build/app/target/jboss-as-console-2.2.0-SNAPSHOT-resources.jar $(JBOSS_PATH)/modules/system/layers/base/org/jboss/as/console/main/

run: install
	$(JBOSS_PATH)/bin/standalone.sh

domain: install
	$(JBOSS_PATH)/bin/domain.sh

test:
	mvn clean install

