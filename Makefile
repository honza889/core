# Path to JBoss AS / EAP / Wildfly
JBOSS_PATH = ../wildfly-8.0.1.Final-SNAPSHOT

all: compile install

compile:
	mvn -Pdev -Dmaven.test.skip=true install

install:
	cp build/app/target/jboss-as-console-2.3.0-SNAPSHOT-resources.jar $(JBOSS_PATH)/modules/system/layers/base/org/jboss/as/console/main/
	cp module.xml $(JBOSS_PATH)/modules/system/layers/base/org/jboss/as/console/main/

standalone:
	$(JBOSS_PATH)/bin/standalone.sh

domain:
	$(JBOSS_PATH)/bin/domain.sh

test:
	cd build/app; mvn gwt:run

