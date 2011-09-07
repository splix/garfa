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
Car.update { Car curr -> //`curr` is a current object, loaded from DB
  curr.count++
}
```

How to use
----------

Just init bean as follows:

```Groovy
@Bean(name = "groovify")
Groobian getGroobian() {
    Groovify groovify = new Groovify()
    groovify.models = [
        Car
    ]
    groovify.objectifyFactory = getDbFactory()
    return groovify
}
```


License
-------

Licensed under the Apache License, Version 2.0

Questions?
----------

Have any questions? Contact me: igor@artamonov.ru