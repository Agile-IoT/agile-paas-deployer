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
