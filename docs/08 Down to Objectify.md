Query
-----

You have direct access to Objectify's Query, by using two following methods:

 * `.findFirst {}`
 * `.findAll {}`

where you can pass the code that can modify any options of passed Query object. Please notice, that query instance,
it's no a passed parameter, your code will operates directly against query instance, as an DSL.

For example:

```groovy
Car.findAll {
  filter('vendor =', 'Ford')
  limit(10)
}
```

More Objectify
--------------

Use method `.withObjectify {}` of a model, this Closure will be called agains Objectify instance, so you can
do whatever you want:

```groovy
CarModel.withObjectify {
  //all methods here are delegated directly to Objectify instance

  //for example method get(...) going to be like ofy.get(...)
  Driver d = get(Driver, 'john')
}
```