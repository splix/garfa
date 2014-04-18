Initialization
--------------

You need to register classes through Garfa on app start.

```groovy
ObjectifyFactory objectifyFactory = //... you need to have Objectify already configured there
Garfa garfa = new Garfa(objectifyFactory)

// Car and Dealer is our models
List<Class> models = [Car, Dealer]

// add magic to our models 
garfa.register(models)
```

### Init as a Spring Framework bean

If you have a Spring Framework app, you could easily initialize Objectify and Garfa with
your @Configuration class, like:

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