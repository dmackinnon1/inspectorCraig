#!/bin/bash
echo 'starting build...'
echo 'cleanup'
rm -f generator.jar
rm -rf target
rm -rf data
mkdir target
mkdir data
echo 'compiling'
$JAVA_HOME/bin/javac -Xlint:unchecked -d target -sourcepath src/inspectorCraigJava/*.java src/inspectorCraigJava/dmackinnon1/craig/*.java
#cd src/inspectorCraigJava

echo 'generating jar'
cd target
$JAVA_HOME/bin/jar cfe ../target/generator.jar dmackinnon1.craig.PuzzleWriter dmackinnon1
cd ..
echo 'executing'
$JAVA_HOME/bin/java -jar target/generator.jar
