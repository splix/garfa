Groovify - Groovy flavour to Objectify
==========================================================

Groovify-Appengine add a DSL to your data models, based on Objectify

Latest version: 0.2-SNAPSHOT

Examples
--------


```Groovy
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

//update (in transaction, tries 3 times)
car.update { Car curr -> //`curr` is a current object, loaded from DB
  curr.count++
}
```

How to use
----------

Just init bean as follows:

```Groovy
@Bean(name = "groovify")
Groovify getGroovify() {
    Groovify groovify = new Groovify()
    groovify.register([
        Car
    ])
    groovify.objectifyFactory = getDbFactory()
    return groovify
}
```

Documentation
-------------

 * [How to store / update data](groovify-appengine/blob/master/docs/updates.markdown)
 * [How to load data from DB](groovify-appengine/blob/master/docs/querying.markdown)


License
-------

Licensed under the Apache License, Version 2.0

Questions?
----------

Have any questions? Contact me: igor@artamonov.ru