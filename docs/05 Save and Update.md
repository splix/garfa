Create a new data object
------------------------

Use `.save()` method

```groovy
car = new Car(
    brand: 'Ford',
    model: 'Mustang',
    count: 0
)
car.save()
```


Update item in transaction
--------------------------

GAE uses optimistic-locking transactions, so, to update an item, Garfa tries to load fresh instance
from DB and execute your code against this instance.

If save of update instance is failed, Garfa retries this steps again, at least 3 times.

### model.update(Closure)

```groovy
car.update { Car loaded ->
  loaded.count++
}
```

### Model.update(id, Closure)

```groovy
Car.update idOrKey, { Car loaded ->
  loaded.count++
}
```

Where:

  * `car` - current instance
  * `idOrKey` - id or Key of instance to update
  * `loaded` - instance loaded for update