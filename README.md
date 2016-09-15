# unified-paas
SeaClouds Unified PaaS Library

---
## 1. Usage
1. Compile needed projects

* https://github.com/rosogon/heroku-java-client. Compile with `mvn install –Dmaven.test.skip`. 

* https://github.com/rosogon/unified-paas. To use the test, API_KEY with heroku key must be set.

2. Start server: `java -jar service/target/unified-paas-service-0.0.1-SNAPSHOT.jar`

3. Deploy an application: 

`curl http://localhost:8080/heroku/applications -X POST -F file=@"<warfile>" -F model='{"name":"samplewar"}' -H"Content-Type: multipart/form-data" -H"apikey:<heroku-api-key>"`

### Integration tests

##### Configuration
* Set values in /library/src/test/resources/tests.config.properties

##### Integration Tests
* Execute tests: `mvn clean verify -P integration-test`

---
## 2. Java Client Libraries
Java client libraries used in the project:

#### Cloud Foundry
+ **Cloud Foundry**:  [Cloud Foundry Java Client](https://github.com/cloudfoundry/cf-java-client)
<dl>
  <dt>Description</dt>
  <dd>The cf-java-client project is a Java language binding for interacting with a Cloud Foundry instance (including PaaS providers like Pivotal, Bluemix etc.).</dd>
  <dt>Features</dt>
</dl>

| Feature       | Supported?    | Comments  |
| ------------- |:-------------:|:-----|
| Application deployment      | :white_check_mark:  | Tested with **Java** and **PHP** apps |
| Start / Stop / Remove |   :white_check_mark:     |   :heavy_minus_sign: |
| Scaling capabilities     | :white_check_mark:       |  Can scale number of instances, disk and RAM |
| Services management |   :white_check_mark:     |   Tested with **ClearDB** from Pivotal |
| Environment variables management |   :white_check_mark:     |   Read & Write |

| Limitations / Comments |
| ------------- ||
| :heavy_minus_sign: |

+ :new: **Cloud Foundry** (new version) :  [Cloud Foundry Java Client](https://github.com/cloudfoundry/cf-java-client)
<dl>
  <dt>Description</dt>
  <dd>The new version of the cf-java-client project is based on Java 8 and [Project Reactor](https://projectreactor.io/) (a second-generation Reactive library for building non-blocking applications on the JVM based on the Reactive Streams Specification).</dd>
  <dt>Features</dt>
  <dd>-Not implemented-</dd>
</dl>
 
---
#### Heroku
+ **Heroku**:  [Heroku JAR](https://github.com/heroku/heroku.jar) & [heroku-maven-plugin](https://github.com/heroku/heroku-maven-plugin)
<dl>
  <dt>Description</dt>
  <dd>[Heroku JAR](https://github.com/heroku/heroku.jar): The Heroku JAR is a java artifact that provides a simple wrapper for the Heroku REST API. The Heroku REST API allows Heroku users to manage their accounts, applications, addons, and other aspects related to Heroku.</dd>
  <dd>[heroku-maven-plugin](https://github.com/heroku/heroku-maven-plugin): This plugin is used to deploy Java applications directly to Heroku without pushing to a Git repository. This is can be useful when deploying from a CI server, deploying pre-built Jar or War files.</dd>
  <dt>Features</dt>
</dl>

| Feature       | Supported?    | Comments  |
| ------------- |:-------------:|:-----|
| Application deployment      | :white_check_mark:  | Tested with **Java** |
| Start / Stop / Remove |   :white_check_mark:     |   :heavy_minus_sign: |
| Scaling capabilities     | :white_check_mark:       |  Can scale number of instances |
| Services management |   :white_check_mark:     |   Tested with **ClearDB** from Pivotal |
| Environment variables management |   :white_check_mark:     |   Read & Write |

| Limitations / Comments |
| ------------- ||
| :heavy_minus_sign: |
---
#### OpenShift
+ **OpenShift v2**:  [OpenShift Java Client](https://github.com/openshift/openshift-java-client) (used by Openshift Online / only for Version 2)
<dl>
  <dt>Description</dt>
  <dd>Java client for the OpenShift REST API. This client is used by JBoss Tools for OpenShift 2.x.</dd>
  <dt>Features</dt>
</dl>

| Feature       | Supported?    | Comments  |
| ------------- |:-------------:|:-----|
| Application deployment      | :white_check_mark:  | :heavy_exclamation_mark: Only from GIT |
| Start / Stop / Remove |   :white_check_mark:     |   :heavy_minus_sign: |
| Scaling capabilities     | :white_check_mark:       |  Can scale number of instances |
| Services management |   :white_check_mark:     |   Tested with **MySQL** from [Openshift Online](https://openshift.redhat.com) |
| Environment variables management |   :x:     |  :heavy_minus_sign:  |

| Limitations / Comments |
| ------------- ||
| :heavy_minus_sign: |
---

+ :new: [Openshift v3](https://github.com/openshift/openshift-restclient-java)  (new version for the version 3 architecture of OpenShift based on Kubernetes: OpenShift Enterprise, local Openshift installations ...)
<dl>
  <dt>Description</dt>
  <dd>-Not implemented-</dd>
  <dt>Features</dt>
  <dd>:heavy_minus_sign:</dd>
</dl>

---

| Client        | Version used  | License | Comments  |
| ------------- |:-------------:| :-------| :---------|
| Cloud Foundry | 1.1.3         | Apache License v2       | :heavy_minus_sign:       |
| :new: Cloud Foundry | :heavy_minus_sign:       | Apache License v2       | :heavy_minus_sign:       |
| Heroku        | 0.16 / 0.5.7**| :heavy_minus_sign: / MIT License**       | :heavy_minus_sign:       |
| Openshift v2  | 2.7.0.Final   | Eclipse Public License v1.0       | :heavy_minus_sign:       |
| :new: Openshift v3  | :heavy_minus_sign:       | :heavy_minus_sign:       | :heavy_minus_sign:       |


** heroku-maven-plugin
