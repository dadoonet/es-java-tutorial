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

Service Layer
=============

Let's add a new Maven module: `tutorial-service':

In `/pom.xml`:

```xml
    <modules>
        <module>tutorial-model</module>
        <module>tutorial-service</module>
    </modules>
```

Create `/tutorial-service/src/main/java` directories.

Create `/tutorial-service/pom.xml` file:

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

    <artifactId>tutorial-service</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.elasticsearch.demo</groupId>
            <artifactId>tutorial-model</artifactId>
            <version>0.1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>

</project>
```

Create `EntityService.java` file:

```java
package org.elasticsearch.demo.service;

import org.elasticsearch.demo.model.bean.Person;

public class EntityService {
    public String save(Person person) {
        // TODO Implement here
        return null;
    }

    public Person get(String id) {
        // TODO Implement here
        return null;
    }

    public void delete(String id) {
        // TODO Implement here
    }
}
```

Add Elasticsearch
-----------------

We are going first to add Elasticsearch as a dependency. We will use at first
an embedded instance of elasticsearch. We will see later on how to connect to a
running cluster.

In `/tutorial-service/pom.xml`, add:`

```xml
    <dependencies>
        ...
        <dependency>
            <groupId>org.elasticsearch</groupId>
            <artifactId>elasticsearch</artifactId>
            <version>0.90.0.RC2</version>
        </dependency>
        ...
    </dependencies>
```

We will create a single instance of Elasticsearch (aka only one node). So we will
create a factory in `/tutorial-service/src/main/java/org/elasticsearch/demo/service/ElasticsearchFactory.java`:

```java
package org.elasticsearch.demo.service;

import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

public class ElasticsearchFactory {
    private static Node node;

    public static Node getNode() {
        if (node == null) {
            // Let's start an Elasticsearch node
            node = NodeBuilder.nodeBuilder().node();
        }

        return node;
    }
}
```

To communicate with Elasticsearch node, we will need a `Client`. We can get one
from any `Node`. Let's share the same `Client` for the whole jvm.

So, we add a `getClient()` method to the same factory.

```java
import org.elasticsearch.client.Client;

public class ElasticsearchFactory {
    private static Node node;

    private static Client client;


    public static Client getClient() {
        if (client == null) {
            client = getNode().client();
        }

        return client;
    }
}
```

Persist entities in Elasticsearch
---------------------------------

Now, we have a running node, we can fill the `EntityService` class.
Remember that we have to give JSon objects to Elasticsearch. So we probably
want to use a JSon library to serialize and deserialize our beans.

Let's add Jackson library in our `/tutorial-model/pom.xml`:

```xml
    <dependencies>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.1.3</version>
        </dependency>
    </dependencies>
```

Let's implement `EntityService#save(Person)` method:

```java
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.elasticsearch.action.index.IndexResponse;

public class EntityService {

    ...

    public String save(Person person) {
        ObjectMapper objectMapper = new ObjectMapper();
        String id = null;
        try {
            IndexResponse indexResponse = ElasticsearchFactory.getClient()
                    .prepareIndex("world", "person")
                    .setSource(objectMapper.writeValueAsBytes(person))
                    .execute().actionGet();
            id = indexResponse.getId();
        } catch (JsonProcessingException e) {
            // We have an exception here. We should handle it...
            throw new RuntimeException("Can not save entity " + person);
        }
        return id;
    }

    ...
}
```

We are using index `world` to persist our entities and we will persist them under type name `person`.


Get entities from Elasticsearch
-------------------------------

Let's implement `EntityService#get(String)` method:

```java
import org.elasticsearch.action.get.GetResponse;

import java.io.IOException;

public class EntityService {

    ...

    public Person get(String id) {
        ObjectMapper objectMapper = new ObjectMapper();
        Person person = null;
        try {
            GetResponse getResponse = ElasticsearchFactory.getClient()
                    .prepareGet("world", "person", id)
                    .execute().actionGet();
            if (getResponse.isExists()) {
                person = objectMapper.readValue(getResponse.getSourceAsBytes(), Person.class);
            }
        } catch (IOException e) {
            throw new RuntimeException("Can not read entity " + id);
        }

        return person;
    }

    ...
}
```
