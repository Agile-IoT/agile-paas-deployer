# unified-paas
Unified PaaS Library

---
## Usage
1. Compile project
2. Start server: `cd service; bin/runserver.sh`
3. Deploy an application: 
    `curl http://localhost:8080/api/heroku/applications -X POST -F file=@"<warfile>" -F model='{"name":"samplewar"}' -H"Content-Type: multipart/form-data" -H'X-PaaS-Credentials:{"api-key":"<heroku-api-key>"}'`

### API Overview

Each supported PaaS is a resource on /api. For example, Heroku is on /api/heroku.

Each invocation to the API must contain the credentials in the X-PaaS-Credentials header, in json format.
See below for specific instructions per provider.

These are the supported operations:

#### GET /api

Return a list of the supported subpaths.

Example:
`curl localhost:8080/api`

`["heroku","cloudfoundry","pivotal","bluemix","openshift2"]`

#### GET /api/{paas}

Return a description of the provider.

#### POST /api/{paas}/applications

Creates an application and deploy its artifact. A multipart request with two fields:
* model: contains information about the application. The name of the application is here, and is used to refer to the application in the rest of operations.
* file: the bytes of the artifact

Examples:

`curl http://localhost:8080/api/heroku/applications -X POST -F file=@"<FILE>" -F model='{"name":"<APP_NAME>"'} -H"Content-Type: multipart/form-data" `

`curl http://localhost:8080/api/pivotal/applications -X POST -F file=@"<FILE>" -F model='{"name":"<APP_NAME>"}' -H"Content-Type: multipart/form-data" `

#### GET /api/{paas}/applications/{name}

Returns the status of an application.

#### DELETE /api/{paas}/applications/{name}

Removes an application

#### PUT /api/{paas}/applications/{name}/start

Starts an application

#### PUT /api/{paas}/applications/{name}/stop

Stops an application

#### PUT /api/{paas}/applications/{name}/scale/{updown}

Scales up/down an application adding or removing an instance. Updown have value `up` or `down`

#### PUT /api/{paas}/applications/{name}/scale/{type}/{value}")

Scales the resource 'type' an application by setting 'value' units.

Supported types:

* instances
* memory
* disk

(TO BE COMPLETED)

### Credentials

Each invocation to the API must contain the credentials in the X-PaaS-Credentials header in the request.
The credentials are serialized in json format, optionally encoded in base64. The fields to be filled in the 
json for each provider are shown below.

#### Heroku
* `api-key`

#### CloudFoundry

* `api`
* `user`
* `password`
* `org`
* `space`

#### Pivotal, BlueMix

* `user`
* `password`
* `org`
* `space`

#### OpenShift2

* `user`
* `password`

### Integration tests

Integration tests are run in `integration-test` profile. Each integration test 
have a group according to the PaaS provider being tested. By default, the 
profile only run tests in dummy group (the dummy provider is a fake provider).
The provider to test can be changed passing an integration.groups parameter
to the mvn command. 

Ex: 
`mvn verify -P integration-test -Dintegration.groups=openshift2,cloudfoundry`


##### Configuration

* Set values in /library/src/test/resources/tests.config.properties
* Start instance of service: `cd service && bin/runserver.sh`

##### Integration Tests

* Execute tests: `mvn clean verify -P integration-test [-Dintegration.groups=...]`

---
## Java Client Libraries
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
