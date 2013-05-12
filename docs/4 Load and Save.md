Get item by ID
--------------

There is two method for loading data from database:

 * .get(id or key)
 * .load(id or key)
 * .getAll(list of ids or keys)

First will throw error if there is no object with specified ID, second just
returns `null` at this case.

```groovy
Long id = 1
Car car = Car.get(id)

Key<Car> carKey = car.key
Car car2 = Car.get(carKey)

car = Car.load(id) //car can be null if there is no entity with id = 1
```

Query
-----

You have direct access to Objectify's Query, by using two following methods:

 * .findFirst {}
 * .findAll {}

where you can pass the code that can modify any options of passed Query object. Please notice, that query instance,
it's no a passed parameter, your code will operates directly against query instance, as an DSL.

For example:

```groovy
Car.findAll {
  filter('vendor =', 'Ford')
  limit(10)
}
```

Find Where
----------

There is an another method for querying:

```groovy
Clazz.findWhere([<fields>], [<params>]) {
  // code executed against Query
}
```

where:

 * fields - list of field filters, where keys is or simple field names (that mean equality filter), or string
    as fieldname + operator. Like: `[model: 'Ford']` or `['model =': 'Ford']` or `['count >': 5]`. First two are
    equal filters
 * optional query parameters - like `[limit: 4]` or `[order: '-count']`
 * closure - more flexibility when you need something specific. It's your code block that will be executed against
    prepared Query. Like `Car.findWhere([], []) { limit(5) }` (btw, it's the same as `.findWhere([], [limit: 5])`)

For example:

```groovy
//get maximum 20 cars where count > 10, ordered by count field, descending
List cars = Car.findWhere(['count >': 10], [order: '-count', limit: 20])
```