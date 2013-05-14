Initialization code
-------------------

```groovy
ObjectifyFactory objectifyFactory = //... you have to init Objectify before Garfa
Garfa garfa = new Garfa(objectifyFactory)

// Car and Dealer is our models
List<Class> models = [Car, Dealer]

// add magic to our models 
garfa.register(models)
```

Use with Spring Framework
-------------------------

### Init as a bean

If you have a Spring Framework app, you could easily initialize Objectify and Garfa with
your Configuration class (for annotation based configuration). Like:

```groovy
@Configuration
class StorageConfig {

    @Bean
    ObjectifyFactory getObjectifyFactory() {
        ObjectifyFactory objectifyFactory = new ObjectifyFactory()
        Garfa garfa = new Garfa(objectifyFactory)
        def models = [
                Car,
                Dealer
        ]
        models.each { Class clz ->
            objectifyFactory.register(clz) // register with Objectify
            garfa.register(clz) // register with Garfa
        }
        return objectifyFactory
    }
    
}
```