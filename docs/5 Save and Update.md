Create new data object
----------------------

Garfa will automaticalyy add .save() method to your domain classes

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

GAE uses optimistic-locking transactions, so, to update an item, we trying to load it
from DB, and execute your code agains this instance. If save is failed, we retry
load-update-save at least 3 times.

```groovy
car.update { Car loaded ->
  loaded.count++
}
```

where:
  * car - current instance
  * loaded - instance loaded for update (can be different from current)

If saving has been failed - it reloads Car from database, and execute your `.count++`
once again