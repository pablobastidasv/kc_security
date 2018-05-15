[![](https://jitpack.io/v/pablobastidasv/kc_security.svg)](https://jitpack.io/#pablobastidasv/kc_security)

# Keycloak security with Soteria (JEE 8)

This project is a library to add security to servlets and JaxRS endpoints using the new security specification 
[JSR-375](https://jcp.org/en/jsr/detail?id=375).  

## Getting started

### Dependency

Add the dependency in your pom as below.

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

```xml
<dependency>
    <groupId>com.github.pablobastidasv</groupId>
    <artifactId>kc_security</artifactId>
    <version>1.0</version>
</dependency>
```

### Configuration

Create a file called `microprofile-config.properties` inside `META-INF` folder and add below properties with values 
corresponding to your configuration.

```properties
security.kc.realm=my-realm
security.kc.authServerUrl=https://my-auth-server/auth
security.kc.clientId=my-client-id
``` 

NOTE: As this project use [MP-Config](http://microprofile.io/project/eclipse/microprofile-config) to set the Keycloak 
configuration.

## Disclaimer

This library has been tested in:

 - [Payara 5](http://www.payara.org/)