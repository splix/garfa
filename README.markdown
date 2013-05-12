Garfa
=====

Garfa - Groovy ActiveRecord for Google Appengine

It's:

 * for Groovy project
 * for Google Appengine
 * based on Objectify

Garfa extends you Groovy class with methods for querying, storing, updating you database.

Latest stable version: 0.4
Latest development version: 0.5-SNAPSHOT
Docs: http://splix.github.io/garfa/

Examples
--------


```groovy
//get by id or key
Long id = 1
Car car = Car.get(id)

Key<Car> carKey = car.key
Car car2 = Car.get(carKey)
assert car == car2

//save
car = new Car(
    brand: 'Ford',
    model: 'Mustang'
)
car.save()

//update (in transaction, with retry)
Car.update id, { Car curr ->
  //`curr` is a current object, loaded from DB
  curr.count++
}
```

Maven dependency
----------------

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


How to use
----------

Just init call it before app start, like:

```groovy
ObjectifyFactory objectifyFactory = //... you have to init Objectify before Garfa
Garfa garfa = new Garfa()
garfa.register([ ... list of models ... ])
garfa.objectifyFactory = objectifyFactory
```

If you're using Spring Framework it could be called on beans initialization (a `@Configuration` class), or
 on `afterPropertiesSet()` for `InitializingBean`

Documentation
-------------

 * http://splix.github.io/garfa/

License
-------

Licensed under the Apache License, Version 2.0

Author
------
[Igor Artamonov](http://igorartamonov.com), [The 6 Hours](http://the6hours.com)