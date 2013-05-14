.beforeSave
-----------

Called on both insert and update

```groovy
class Car {
  void beforeSave() {
    ...
  }
}
```

.beforeInsert
-------------

Called before first save (when Id is null)

```groovy
class Car {
  void beforeInsert() {
    ...
  }
}
```


.beforeUpdate
-------------

Called before object update

```groovy
class Car {
  void beforeUpdate() {
    ...
  }
}
```
