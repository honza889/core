# Path to JBoss AS / EAP / Wildfly
JBOSS_PATH = ../wildfly-8.0.0.CR1

all: install domain

compile:
	mvn -Pdev -Dmaven.test.skip=true install

install: compile
	cp build/app/target/jboss-as-console-2.2.0-SNAPSHOT-resources.jar $(JBOSS_PATH)/modules/system/layers/base/org/jboss/as/console/main/

run:
	$(JBOSS_PATH)/bin/standalone.sh

domain:
	$(JBOSS_PATH)/bin/domain.sh

test:
	mvn clean install

