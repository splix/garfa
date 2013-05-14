About Garfa
-----------

Garfa - is Groovy ActiveRecord for Google Appengine

It's a tiny wrapper around Objectify 3, and should work with any Groovy project for Appengine. It's pretty
 safe to use Garfa in your project, because all underlying work is done by well-tested Objectify, and
 if you have something very specific you could always dig down to Objectify.

Garfa extends you Groovy class with methods for querying, storing and updating models for Appengine database.

Download
--------

### Maven dependency

```xml
<dependency>
    <groupId>com.the6hours</groupId>
    <artifactId>garfa</artifactId>
    <version>0.5-SNAPSHOT</version>
</dependency>
```

```xml
<repositories>
    <repository>
        <id>the6hours-release</id>
        <url>http://maven.the6hours.com/release</url>
        <releases><enabled>true</enabled></releases>
        <snapshots><enabled>false</enabled></snapshots>
    </repository>
</repositories>
```

### Sources

https://github.com/splix/garfa/

License
-------

Project is licensed under Apache 2 license.