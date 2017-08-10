FROM resin/raspberry-pi3-openjdk:openjdk-8-jdk-20170426

RUN apt-get update && apt-get install --no-install-recommends -y \
    maven\
    && apt-get clean && rm -rf /var/lib/apt/lists/*

COPY . /usr/local/src/

WORKDIR /usr/local/src
RUN mvn package

ENV src_jar service/target/unified-paas-service.jar
ENV src_config service/conf/config.yml
ENV dest /opt/unified-paas

RUN mkdir -p ${dest}
RUN cp ${src_jar} ${src_config} ${dest}

ENTRYPOINT [ \
  "java", "-jar", \
  "/opt/unified-paas/unified-paas-service.jar", \
  "server", "/opt/unified-paas/config.yml"]
