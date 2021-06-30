## Rest API SDK Core

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.taboola/api-java-client-core/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.taboola/api-java-client-core)
[![Build Status](https://travis-ci.org/taboola/api-java-client-core.svg?branch=master)](https://travis-ci.org/taboola/api-java-client-core)

### Table of Contents
1. [Getting Started](#1-getting-started)
2. [Exceptions](#2-exceptions)
3. [Usage](#3-usage)

### Intro
Easily create Rest API clients by defining endpoint mappings and models that is going to be filled by JSON over HTTP

## 1. Getting Started

### 1.1 Create Rest Client
First you will need _RestAPIClient.java_ object
```
RestAPIClient restClient = RestAPIClient.builder()
                                        .setBaseUrl('your_base_url')
                                        .build();
```

### 1.2 Create Model And Mapping
To know what is possible when creating endpoints interface please refer to [Retrofit2 documentation](https://square.github.io/retrofit/)
```
public class EntityModelExample {
  private String id;
  private String name;

  public String getId() { return id; }
  public String setId(String id) { this.id = id; }
  public String getName() { return name; }
  public String setName(String name) { this.name = name; }
}

public interface EntityModelEndpoint {

    @POST("/{account_id}")
    @Headers("Content-Type: application/json")
    EntityModelExample update(@Header("Authorization") String accessToken,
                             @Path("account_id") String accountId,
                             @Body EntityModelExample entity) throws RestAPIException;
}
```

### 1.3 Create Rest Client Endpoint
```
EntityModelEndpoint endpoint = restClient.createRetrofitEndpoint(EntityModelEndpoint.class);
// use endpoint...
```

### 2. Exceptions

- **RestAPIUnauthorizedException** - Token is expired or bad credentials were supplied (HTTP status 401)
  - Can be resolved by re-authentication or making sure that supplied credentials are correct
- **RestAPIRequestException** - Bad request (HTTP status 4xx)
  - Can be resolved by fixing the request to a valid one
- **RestAPIConnectivityException** - Connectivity issues (HTTP status 5xx)
  - Can be resolved by retrying or fixing networking issues

### 3. Usage

If your project is built with Maven add following to your pom file:

```
<dependency>
    <groupId>com.taboola</groupId>
    <artifactId>api-java-client-core</artifactId>
    <version>x.y.z</version>
</dependency>
```

If your project is built with Gradle add following to your gradle setting file:

```
// https://mvnrepository.com/artifact/com.taboola/api-java-client-core
compile group: 'com.taboola', name: 'api-java-client-core', version: 'x.y.z'
```

Replace 'x.y.z' with the latest available version from [Maven Central](https://mvnrepository.com/artifact/com.taboola/api-java-client-core)
