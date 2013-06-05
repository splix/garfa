About Garfa
-----------

Garfa - is Groovy ActiveRecord for Google Appengine

It's a tiny wrapper around Objectify 3, and should work with any Groovy project for Appengine. It's pretty
 safe to use Garfa in your project, because all underlying work is done by well-tested Objectify, and
 if you have something very specific you could always dig down to Objectify.

Garfa extends your database models with methods for querying, storing and updating models for Appengine database.

Download
--------

### Maven dependency

```xml
<dependency>
    <groupId>com.the6hours</groupId>
    <artifactId>garfa</artifactId>
    <version>0.5</version>
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



----


Hi!

I'd like to introduce my project called Garfa: https://github.com/splix/garfa

Garfa is a Active Record for Groovy on Appengine, it extends your Groovy classes with methods for querying, storing and updating models for Appengine database.

Basically it allows you to write code like:
CarModel mustang = new CarModel(vendor: 'Ford', model: 'Mustang', year: 2012)
mustang.save()
Car redMustang = new Car(model: mustang.key, price: 22000, color: 'red')
redMustang.save()

Car blackMustang = Car.findFirstByModelAndColor(mustang.key, 'black')


It's a tiny wrapper around Objectify 3.1, and should work with any Groovy project for Appengine. The project is in early stage of development, but it's pretty safe to use Garfa in your project, because all underlying work is done by well-tested Objectify, and if you have something very specific you could always dig down to Objectify.

GitHub: https://github.com/splix/garfa
Documentation: http://splix.github.io/garfa/
License: Apache 2.0