#!/usr/bin/env bash
#
#  Copyright (c) 2018 Atos, and others
#  All rights reserved. This program and the accompanying materials
#  are made available under the terms of the Eclipse Public License 2.0
#  which accompanies this distribution, and is available at
#  https://www.eclipse.org/legal/epl-2.0/
#
#  Contributors:
#  Atos - initial implementation
#

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
