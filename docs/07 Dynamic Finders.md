Garfa supports dynamic finders like:

```groovy
Car.findByModelAndYear("Mustang", 2008)
```

Model.findBy*(...), Model.findBy*(..., options)
-----------------------------------------------

Return list of entries, filtered by specified fields

Options is a optional argument, it's a `Map` with following possible entries:

 * `limit` - max number of elements, like `[limit: 2]`
 * `offset` - initial offset, like `[offset: 10]`
 * `sort` - sort by field value. Value of this option is a field name to use sort for sorting. By default
   it sorts in ascending order, to use descending use `-` as a prefix to field name.
   Like `[sort: 'model`], `[sort: '-year']`
 * `cursor` - string value or `Cursor` instance to use for this query

Model.findFirstBy*(...), Model.findFirstBy(..., options)
--------------------------------------------------------

Same as `findBy`, but returns first element only. Or `null` if not found.

Examples
--------

```groovy
Car car1 = Car.findFirstByVendor('Vaz')
Car cheapFord = Car.findFirstByVendor('Ford', [sort: 'price'])

List<Car> allFords = Car.findByVendor('Ford')
List<Car> firstPageFords = Car.findByVendor('Ford', [limit: 10])
```

