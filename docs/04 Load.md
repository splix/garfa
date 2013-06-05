Get item by ID
--------------

There is two method for loading data from database:

### Model.get(id or key)

Will throw error if there is no entity with specified ID

```groovy
Long id = 1
try {
  Car car = Car.get(id)
} catch (NotFoundException e) {
  ...
}

Key<Car> carKey = new Key<Car>(Car, 1)
try {
  Car car2 = Car.get(carKey)
} catch (NotFoundException e) {
  ...
}
```

### Model.load(id or key)

Will returns `null` if there are no entity with specified ID.

```groovy
Long id = 1
Car car = Car.load(id)
if (car == null) {
   ... not found
}

Key<Car> carKey = new Key<Car>(Car, 1)
Car car2 = Car.load(carKey)
if (car2 == null) {
   ... not found
}
```

### Model.getAll(list of ids or keys)

Loads list of entities for specified ids:

```groovy
List<Car> cars = Car.getAll([1, 2, 3])
```

Get a Query for a Model
-----------------------

You could get a Objectify Query for a model:

```groovy
Query<Model> query = Model.queryWhere([<fields>], [<params>])
```

where:

 * fields - list of field filters, where keys is or simple field names (that mean equality filter), or string
    as fieldname + operator. Like: `[model: 'Ford']` or `['model =': 'Ford']` or `['count >': 5]`.
 * optional query parameters - like `[limit: 4]` or `[order: '-count']`


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

Find by Ancestor
----------------

```groovy
//a parent instance
CarModel fordFocus

//find by parent:
List<Car> cars = Car.findByAncestor(fordFocus)

//or by a parent key:
List<Car> cars = Car.findByAncestor(fordFocus.key)
```