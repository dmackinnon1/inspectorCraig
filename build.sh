#!/bin/bash
# ensure that JAVA_HOME environment variable is set in order to execute the build commands.
echo '...starting build'

echo '...cleanup'
rm -rf target
rm -rf data
mkdir target
mkdir data

echo '...compiling sources from src/inspectorCraigJava to target directory'
#$JAVA_HOME/bin/javac -Xlint:unchecked -d target -sourcepath src/inspectorCraigJava/*.java src/inspectorCraigJava/dmackinnon1/craig/*.java
$JAVA_HOME/bin/javac -Xlint:unchecked -sourcepath src/inspectorCraig -d target src/inspectorCraigJava/*/*/*.java
echo '...creating generator.jar in target directory'
cd target
$JAVA_HOME/bin/jar cfe ../target/generator.jar dmackinnon1.puzzles.PuzzleWriter dmackinnon1
cd ..

echo '...executing jar to generate puzzle data in data directory'
$JAVA_HOME/bin/java -jar target/generator.jar "$1"

echo '...complete'
