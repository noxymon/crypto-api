#!/usr/bin/env bash

ARTIFACT=crawler-historical-data
MAINCLASS=id.noxymon.miner.crawler.CrawlerHistoricalDataApplication
VERSION=0.0.1-SNAPSHOT

GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

rm -rf target
mkdir -p target/native-image

echo "Packaging $ARTIFACT with Maven"
mvn -ntp package -DskipTests > target/native-image/output.txt

JAR="$ARTIFACT-$VERSION.jar"
rm -f $ARTIFACT
echo "Unpacking $JAR"
cd target/native-image
jar -xvf ../$JAR >/dev/null 2>&1
cp -R META-INF BOOT-INF/classes

LIBPATH=`find BOOT-INF/lib | tr '\n' ':'`
CP=BOOT-INF/classes:$LIBPATH

GRAALVM_VERSION=`native-image --version`
echo "Compiling $ARTIFACT with $GRAALVM_VERSION"
{ time native-image \
  --verbose \
  -H:Name=$ARTIFACT \
  -Dspring.native.remove-yaml-support=true \
  --enable-all-security-services \
  -J-Xmx8G \
  -Dspring.graal.remove-unused-autoconfig=true \
  -Dspring.graal.skip-logback=true \
  -Dspring.native.remove-yaml-support=true \
  -Dspring.spel.ignore=true \
  -Dspring.native.remove-jmx-support=true \
  -Dspring.native.remove-xml-support=true \
  -cp $CP $MAINCLASS >> output.txt ; } 2>> output.txt

if [[ -f $ARTIFACT ]]
then
  printf "${GREEN}SUCCESS${NC}\n"
  mv ./$ARTIFACT ..
  exit 0
else
  cat output.txt
  printf "${RED}FAILURE${NC}: an error occurred when compiling the native-image.\n"
  exit 1
fi