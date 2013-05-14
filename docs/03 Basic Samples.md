Models
-------
```groovy

class CarModel {

    @Id
    Long id

    String vendor
    String model
    int year

    void beforeInsert() {
        if year == 0 {
            year = 1896
        }
    }

}

class Car {

    @Id
    Long id
    @Parent
    Key<CarModel> model

    int price
    String color
}
```

Create entities
---------------

```groovy
CarModel mustang = new CarModel(vendor: 'Ford', model: 'Focus', year: 2012)
mustang.save()
Car redMustang = new Car(model: mustang.key, price: 22000, color: 'red')
redMustang.save()
```

Update
------

Make a discount!!! $22000 -> $21000:

```groovy
redMustang.update {
  price = 21000
}
```

Find
----

```groovy
Car blackMustang = Car.findFirstByModelAndColor(mustang.key, 'black')

// load model with ID 5161
CarModel foo = CarModel.load(5161)
```