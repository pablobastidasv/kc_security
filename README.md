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
    <version>1.2</version>
</dependency>
```

### Configuration

Keycloak security library could be configured through `keycloak.json` file or define main values as environment 
variable.

#### With specified `keycloak.json` file

**NOTE**: This approach supports full capabilities of `keycloak-servlet-filter-adapter`, the keycloak configuration 
file is defined and used in total.

Create a file called `microprofile-config.properties` inside `META-INF` folder and add below property, this value 
should be the full path to `keycloak` file location.

```properties
security.kc.file-path=/opt/keycloak.json
``` 

#### With `keycloak.json` file in default location

`keycloak.json` file could be also created in resource folder inside war file to be used as configuration in 
case `security.kc.file-path` is not specified.

#### With environment variables

**NOTE**: This approach just define basic values to work in development environment or to test applications.  

Create a file called `microprofile-config.properties` inside `META-INF` folder and add below properties with values 
corresponding to your configuration.

```properties
security.kc.realm=my-realm
security.kc.authServerUrl=https://my-auth-server/auth
security.kc.clientId=my-client-id
``` 

**NOTE**: As this project use [MP-Config](http://microprofile.io/project/eclipse/microprofile-config) to set the Keycloak 
configuration.

## Tested platforms

This library has been tested in:

 - [Payara 5](http://www.payara.org/)