# Descoped Embedded Server

[![Build Status](https://travis-ci.org/descoped/descoped-container.svg?branch=master)](https://travis-ci.org/descoped/descoped-container)

The Descoped Embedded Server is a tiny web server that bootstraps Weld Cdi Container, Undertow and adds basic support for making a web application.

## Getting started

## Maven dependency

```xml
<dependency>
    <groupId>io.descoped</groupId>
    <artifactId>container</artifactId>
    <version>1.0.1</version>
    <scope>compile</scope>
</dependency>
```

## Build

#### Build and install
`mvn clean install`

#### Run test suite

`mvn clean test`

#### Run a specific test

`mvn clean test -Dtest=ContainerTest`

#### Build all and skip tests
`mvn clean install -DskipTest`

#### Start server

`./run.sh`

#### Start server and deploy (only useful for when testing the `descoped-server` module itself)

`./run.sh -Dorg.apache.deltaspike.ProjectStage=DaemonTest`

## Usage

### Configuration (application.properties)

```
descoped.server.host=0.0.0.0
descoped.server.port=8080
descoped.server.contextPath=/
descoped.server.jaxRsPackages=io.descoped
descoped.server.maxWorkers=0
```

### @WebServer annotation

Each ServerContainer is designated an `id`, which defaults to `@WebServer(id = "default")`. If you need to have multiple container instances running, then you must assign a new id for the other container. E.g. `@WebServer(id = "my-container")`.  

### TestControl

A random http port of 8080 + random(150) will be designated for each running instance during testing.



## Examples

```java
@RunWith(CdiTestRunner.class)
@TestControl(logHandler = ConsoleAppender.class)
public class MyTest {

    @Inject
    @WebServer
    ServerContainer container;

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
        given()
            .port(container.getPort())
            .contentType(ContentType.XML.withCharset("UTF-8"))
                .log().everything()
            .expect()
                .statusCode(HttpURLConnection.HTTP_OK)
                .log().everything()
            .when()
                .get("/test/")
            ;
    }        
}
```

Deployment can also be handled when the event observers are fired.

### Pre-startup container

```java
    public void onStartup(@Observes PreStartContainer event) {
        event.container().deploy(new RestDeployment());
    }
```

### Pre-shutdown container

```java
    public void onShutdown(@Observes PreStopContainer event) {
        // do something
    }
```

