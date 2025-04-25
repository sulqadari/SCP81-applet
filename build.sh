#!/usr/bin/bash

export JAVA_HOME=/usr/bin/java
export PATH=$JAVA_HOME/bin:$PATH

export JC_HOME=./libraries/jc304_kit
export PATH=$JC_HOME/bin:$PATH

if [ -d build ]; then
	rm -rf build
fi
mkdir build

source_path=./src/inter/galaxy/me/Main.java
build_dir=./build

applet_name=Main
package_name=inter.galaxy.me
gp_lib_dir=./libraries/GP-v171/1.7

jc_jar=./libraries/jc304_kit/lib/api_classic.jar
gp_jar=./libraries/GP-v171/1.7/gpapi-globalplatform.jar
uicc_jar=./libraries/UICC-API/uicc-api-for-java-card-REL-15_2.jar
usim_jar=./libraries/USIM-API/usim-api-for-java-card-REL-15_2.jar

jc_exports=./libraries/jc304_kit/api_export_files
uicc_exports=./libraries/UICC-API/exports
usim_exports=./libraries/USIM-API/exports
gp_exports=./libraries/GP-v171/1.7/exports

# Project-specific export files
export_files=$gp_exports:$usim_exports:$jc_exports:$uicc_exports

#Project-specific jar files
jar_files=$jc_jar:$gp_jar:$uicc_jar:$usim_jar

javac -Xlint:-options -Xlint:deprecation -g -d $build_dir -classpath $jar_files -sourcepath ./src \
-source 1.5 -target 1.5 $source_path

jc_converter=./libraries/jc304_kit/lib/tools.jar

java -Djc.home=$JC_HOME -cp $jc_converter com.sun.javacard.converter.Main -i -classdir $build_dir -out CAP EXP JCA -exportpath $export_files \
-applet 0xA0:0x00:0x00:0x00:0x00:0x00:0x00:0x00 $applet_name \
$package_name 0xA0:0x00:0x00:0x00:0x00:0x00:0x00:0x00:0x01 1.0