#!/usr/bin/env bash
set -u

DIR=$(cd "$(dirname "$0")/.." && pwd)

cd $DIR
test -d target && { echo "dist/target exists. Please remove manually"; exit 1; }
mkdir target

# Create dir structure in target
cp -R src/* target
mkdir -p target/logs
mkdir -p target/run
mkdir -p target/etc

# Compiles core
cd $DIR/..
mvn -Dmaven.test.skip clean install

# Copy core files
cd $DIR
cp ../service/target/unified-paas-service-0.0.1-SNAPSHOT.jar target/bin/
cp ../service/conf/config.yml target/etc/

# Create zip
cd target
zip -r paas-unified.zip .
