Models
-------
```groovy

@Entity
class CarModel {

    @Id
    Long id

    @Index
    String vendor
    @Index
    String model
    @Index
    int year

    void beforeInsert() {
        if year == 0 {
            year = 1896
        }
    }

}

@Entity
class Car {

    @Id
    Long id
    @Parent
    Key<CarModel> model

    @Index
    int price
    @Index
    String color
}
```

Create entities
---------------

```groovy
CarModel mustang = new CarModel(vendor: 'Ford', model: 'Mustang', year: 2012)
mustang.save(flush: true) //sync save
Car redMustang = new Car(model: mustang.key, price: 22000, color: 'red')
redMustang.save() //async save
```

Update
------

Current instance of entity have `.update` method, that accept a Closure that will update db. Notice, that this
method will load a fresh version from DB, and try to update. Also it will try to perform this operation up to 3 times
if fail (for the situation when you're updating same entity from different threads)

Ok, lets make a discount!!! $22000 -> $21000:

```groovy
Car withDiscount = redMustang.update {
  price = 21000
}
```

`.update` method also return updated instance. Or throw exception if failed to update (only when all
3 tries are failed).

Find
----

```groovy
Car blackMustang = Car.findFirstByModelAndColor(mustang.key, 'black')

// load model with ID 5161
CarModel foo = CarModel.load(5161)

List<Car> allYellow = Car.findAllByColor('yellow')
```

`Model`, `Color`, etc is a entity fields to filter.
