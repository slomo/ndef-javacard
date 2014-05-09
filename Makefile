JC_HOME=$(HOME)/opt/java-card-sdk
JAVA_HOME=/

JAVAC=$(JAVA_HOME)/bin/javac -source 1.2 -target 1.2

CL_DIR=$(PWD)/bin
JC_PATH=$(JC_HOME)/lib/api.jar:$(CL_DIR)
JCFLAGS=-g -d $(CL_DIR) -classpath $(JC_PATH)

# project settings
PACKAGE=de/spline/uves/Ndef

all: Ndef.cap

bin/$(PACKAGE)/Ndef.class: src/$(PACKAGE)/Ndef.java
	$(JAVAC) $(JCFLAGS) src/$(PACKAGE)/Ndef.java

Ndef.cap: bin/$(PACKAGE)/Ndef.class
	$(JC_HOME)/bin/converter -out CAP -exportpath . -classdir ./bin -d ./cap \
		-applet 0xa0:0x0:0x0:0x0:0x62:0x3:0x1:0xc:0x1:0x1 de.spline.uves.ndef.Ndef \
		-exportpath /home/yves/opt/java-card-sdk/api_export_files \
		-nobanner \
		de.spline.uves.ndef \
		0xa0:0x0:0x0:0x0:0x62:0x3:0x1:0xc:0x1 1.0
