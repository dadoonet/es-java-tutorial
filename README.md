Elasticsearch Java Tutorial
===========================

Welcome to this Java Tutorial. Main goal is here to provide a step by step guide to use
Elasticsearch in a Java project.


Abstract
========

We are going to index `person` entities in Elasticsearch. Our goal is to index and provide a full text search
web application.
So, we will have to build two modules:

- dto layer to define our beans: `tutorial-model`
- service layer to manage our beans: `tutorial-service`
- web application layer to connect our GUI with tutorial-service: `tutorial-webapp`

So, let's define first a `/pom.xml` file:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.elasticsearch.demo</groupId>
    <artifactId>elasticsearch-java-tutorial</artifactId>
    <version>0.1.0-SNAPSHOT</version>
    <packaging>pom</packaging>

</project>
```

DTO Layer
=========

Let's add a new Maven module: `tutorial-model':

In `/pom.xml`:

```xml
    <modules>
        <module>tutorial-model</module>
    </modules>
```

Create `/tutorial-model/src/main/java` directories.

Create `/tutorial-model/pom.xml` file:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <artifactId>elasticsearch-java-tutorial</artifactId>
        <groupId>org.elasticsearch.demo</groupId>
        <version>0.1.0-SNAPSHOT</version>
    </parent>

    <artifactId>tutorial-dto</artifactId>

</project>
```

Add our bean classes in `/tutorial-model/src/main/java` dir:

File `org/elasticsearch/demo/model/bean/Address.java`:

```java
package org.elasticsearch.demo.model.bean;

public class Address {
    private String country;
    private String zipcode;
    private String city;

    // Skip getters and setters
}
```

File `org/elasticsearch/demo/model/bean/Person.java`:

```java
package org.elasticsearch.demo.model.bean;

import java.util.Date;

public class Person {
    private String name = null;
    private Date dateOfBirth = null;
    private String gender = null;
    private Address address;

    // Skip getters and setters
}
```

