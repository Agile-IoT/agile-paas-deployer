FROM resin/raspberrypi3-openjdk:openjdk-8-jdk-20170217

ENV src_jar service/target/unified-paas-service.jar
ENV src_config service/conf/config.yml
ENV dest /opt/unified-paas

RUN mkdir -p ${dest}
COPY ${src_jar} ${dest}/unified-paas-service.jar
COPY ${src_config} ${dest}/config.yml

ENTRYPOINT [ \
  "java", "-jar", \
  "/opt/unified-paas/unified-paas-service.jar", \
  "server", "/opt/unified-paas/config.yml"]
