#!/usr/bin/env bash
set -u
set -e

SKIP_INSTALL=""
SKIP_TESTS=""
if [ ${1:-""} = "skip-install" ]; then
    SKIP_INSTALL=1
elif [ ${1:-""} = "skip-tests" ]; then
    SKIP_TESTS="-Dmaven.test.skip"
fi


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
[ $SKIP_INSTALL ] || mvn $SKIP_TESTS clean install

# Copy core files
cd $DIR
cp ../service/target/unified-paas-service.jar target/bin/
cp ../service/conf/config.yml target/etc/

# Create zip
cd target
zip -r paas-unified.zip .
