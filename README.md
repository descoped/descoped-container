# Descoped Embedded Server

[![Build Status](https://travis-ci.org/descoped/descoped-server.svg?branch=master)](https://travis-ci.org/descoped/descoped-server)

The Descoped Embedded Server is a tiny web server that bootstraps Weld Cdi Container, Undertow and adds basic support for making a web application.

# Getting started

## Maven dependency

```xml
<dependency>
    <groupId>io.descoped</groupId>
    <artifactId>server</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```

## Build

`mvn clean install`

`mvn clean test`

`mvn clean test -Dtest=ContainerTest`

`mvn clean install -DskipTest`

`./run.sh`

`./run.sh -Dorg.apache.deltaspike.ProjectStage=RestDeployment`


# Exampels

```java
@RunWith(CdiTestRunner.class)
@TestControl(logHandler = ConsoleAppender.class)
public class ServerExtTest {

    @Inject
    @WebServer
    ServerContainer server;

    @Before
    public void setUp() throws Exception {
        container.deploy(new RestDeployment());
        container.start();
    }

    @After
    public void tearDown() throws Exception {
        container.shutdown();
    }

    @Test
    public void first_run() throws Exception {
        // do something
    }        
}
```

you may also alternately deploy before calling start:

```java
    public void handleDeployment(@Observes PreStartContainer event) {
        event.container().deploy(new RestDeployment());
    }
```

```java
    public void handleDeployment(@Observes PreStopContainer event) {
        event.container().deploy(new RestDeployment());
    }
```
