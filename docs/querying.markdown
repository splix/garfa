Querying
========

Get item by ID
--------------

There is two method for loading data from database:
 * .get(id or key)
 * .load(id or key)

First will throw error if there is no object with specified ID, second just
returns `null` at this case.

```Groovy
Long id = 1
Car car = Car.get(id)

Key<Car> carKey = car.key
Car car2 = Car.get(carKey)

car = Car.load(id) //car can be null if there is no entity with id = 1
```

Query
-----