#!/usr/bin/bash

if [ -z "${JAVA_HOME}" ]; then
	export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64
	export PATH=$JAVA_HOME/bin:$PATH
fi

if [ -z "${JC_HOME}" ]; then
	export JC_HOME=./libraries/jc304_kit
	export PATH=$JC_HOME/bin:$PATH
fi

if [ -d build ]; then
	rm -rf build
fi

mkdir build
source_path=./src/Main.java
applet_name=Main
package_name=
libs_fldr=./libraries
build_fldr=./build
exports_fldr=$libs_fldr/jc304_kit/api_export_files:$libs_fldr/UICC-API/exports
jar_files=$libs_fldr/jc304_kit/lib/api_classic.jar:$libs_fldr/UICC-API/uicc-api-for-java-card-REL-15_2.jar

javac -Xlint:-options -Xlint:deprecation -g -d $build_fldr -classpath $jar_files -sourcepath ./src \
-source 1.5 -target 1.5 $source_path

$JC_HOME/bin/converter -i -classdir $build_fldr -out CAP EXP JCA -exportpath $exports_fldr \
-applet 0xB0:0x00:0x00:0x00:0x81:0x0B:0x52:0x81:0x00:0x00:0x01:0x81:0x42:0x49:0x44:0x08 $applet_name \
$package_name 0xB0:0x00:0x00:0x00:0x81:0x0B:0x52:0x81:0x00:0x00:0x01:0x81:0x42:0x49:0x44:0x00 1.0