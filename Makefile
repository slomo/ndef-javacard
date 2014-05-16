JC_HOME=$(HOME)/opt/java-card-sdk
JAVA_HOME=/
JCARDSIM_JAR=~/opt/jcardsim-2.2.1-all.jar

JAVAC=$(JAVA_HOME)/bin/javac -source 1.2 -target 1.2

CL_DIR=./bin
JC_PATH=$(JC_HOME)/lib/api.jar:$(CL_DIR)
JCFLAGS=-g -d $(CL_DIR) -classpath $(JC_PATH)

# project settings
PACKAGE=de/spline/uves/ndef
PACKAGE_DOTS=de.spline.uves.ndef
AID=0xd2:0x76:0x00:0x00:0x85:0x01:0x01

all: Ndef.cap

bin/$(PACKAGE)/Ndef.class: src/$(PACKAGE)/Ndef.java
	$(JAVAC) $(JCFLAGS) src/$(PACKAGE)/Ndef.java

cap/$(PACKAGE)/javacard/ndef.cap: bin/$(PACKAGE)/Ndef.class
	$(JC_HOME)/bin/converter -out CAP -exportpath . -classdir ./bin -d ./cap \
		-applet $(AID) $(PACKAGE_DOTS).Ndef \
		-exportpath /home/yves/opt/java-card-sdk/api_export_files \
		-nobanner \
		$(PACKAGE_DOTS) \
		$(AID):0x01 1.0

test: bin/$(PACKAGE)/Ndef.class test/$(PACKAGE)/NdefSpec.scala
	scala -cp $(JCARDSIM_JAR):$(CL_DIR):/home/yves/opt/scalatest_2.11-2.1.5.jar:$(PWD)/test org.scalatest.run NdefSpec
#	@echo "com.licel.jcardsim.card.applet.0.AID=d2760000850101" > test/jcardsim.conf
#	@echo "com.licel.jcardsim.card.applet.0.Class=de.spline.uves.ndef.Ndef" >> test/jcardsim.conf
#	java -cp $(JCARDSIM_JAR):$(CL_DIR) com.licel.jcardsim.utils.APDUScriptTool test/jcardsim.conf test/script.apdu

Ndef.cap: cap/$(PACKAGE)/javacard/ndef.cap
	cp cap/$(PACKAGE)/javacard/ndef.cap Ndef.cap

.PHONY: test
