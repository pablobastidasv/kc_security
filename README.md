[![](https://jitpack.io/v/pablobastidasv/kc_security.svg)](https://jitpack.io/#pablobastidasv/kc_security)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.pablobastidasv/kc_security.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.pablobastidasv%22%20AND%20a:%22kc_security%22)

# Keycloak security with Soteria (JEE 8)

This project is a library to add security to servlets and JaxRS endpoints using the new security specification 
[JSR-375](https://jcp.org/en/jsr/detail?id=375).  

## Getting started

### Dependency

#### Maven central

Add below dependency to your pom.

```xml
<dependency>
    <groupId>io.github.pablobastidasv</groupId>
    <artifactId>kc</artifactId>
    <version>{version}</version>
</dependency>
```

#### Jitpack

To older versions use jitpack. Add the dependency in your pom as below.

**NOTE**: Latest version published on Jetpack.io is 1.2.1

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
    <version>{verion}</version>
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

## JwtPrincipal

As a `Principal`, is provided an extension with `JwtPrincipal` which can be injected in your beans via CDI.

```java
@Inject
private JwtPrincipal principal;
```

The `JwtPrincipal` provides util information about the logged user. Bellow this class' attributes: 

```java
String userName;
String fullName;
String givenName;
String familyName;
String email;
String picture;
String token;
Map<String, Object> claims;
```

|    Key    | Description |
|:---------:|-------------|
|userName   | Keycloak JWT value of: preferred_username
|fullName   | Keycloak JWT value of: name
|givenName  | Keycloak JWT value of: given_name
|familyName | Keycloak JWT value of: family_name
|email      | Keycloak JWT value of: email
|picture    | Keycloak JWT value of: picture
|token      | The JWT token
|claims     | Map of any other claims and data that might be in the IDToken. Could be custom claims set up by the auth server

## Tested platforms

This library has been tested in:

 - [Payara 5](http://www.payara.org/)
 - [Openliberty 18.0.0.3](https://openliberty.io)
