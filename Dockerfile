#-------------------------------------------------------------------------------
# Copyright (C) 2017 Atos, and others
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License 2.0
# which accompanies this distribution, and is available at
# https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
# 
# Contributors:
# Atos - initial implementation
#-------------------------------------------------------------------------------
ARG BASEIMAGE_BUILD=agileiot/intel-nuc-zulujdk:8-jdk-maven
ARG BASEIMAGE_DEPLOY=agileiot/intel-nuc-zulujdk:8-jre
#ARG BASEIMAGE_DEPLOY=agileiot/raspberry-pi3-zulujdk:8-jre
FROM $BASEIMAGE_BUILD

ENV BUILD_PATH /usr/local/src

WORKDIR $BUILD_PATH
COPY . $BUILD_PATH

RUN mvn package


FROM $BASEIMAGE_DEPLOY

ENV BUILD_PATH /usr/local/src
ENV DEPLOY_PATH /opt/unified-paas
WORKDIR $DEPLOY_PATH

COPY --from=0 $BUILD_PATH/service/target/unified-paas-service.jar  unified-paas-service.jar
COPY --from=0 $BUILD_PATH/service/conf/config.yml config.yml

ENTRYPOINT [ \
  "java", "-jar", \
  "/opt/unified-paas/unified-paas-service.jar", \
  "server", "/opt/unified-paas/config.yml"]
