package com.the6hours.garfa

import com.googlecode.objectify.ObjectifyFactory

/**
 * TODO
 *
 * @since 28.08.11
 * @author Igor Artamonov (http://igorartamonov.com)
 */
class Garfa {

    GarfaBasicDsl basicDsl = new GarfaBasicDsl()
    GarfaFindDsl  findDsl = new GarfaFindDsl()
    GarfaDynamicFindDsl dynamicFindDsl = new GarfaDynamicFindDsl()

    static final Map defaults = [
            'get'    : [safe: true],
            'save'   : [flush: true],
            'delete' : [flush: true],
            'update' : []
    ]

    Garfa() {
    }

    Garfa(ObjectifyFactory factory) {
        this.setObjectifyFactory(factory)
    }

    void register(List<Class> models) {
        models.each {
            register(it)
        }
    }

    void register(Class model) {
        basicDsl.extend(model)
        findDsl.extend(model)
        model.metaClass.'static'.methodMissing = { String name, args ->
            if (dynamicFindDsl.supports(name)) {
                return dynamicFindDsl.tryExecute(model, name, (List) args)
            } else {
                new MissingMethodException(name, delegate, args)
            }
        }
    }

    void setObjectifyFactory(ObjectifyFactory ofy) {
        Holder._objectifyFactory = ofy
    }

    static def defaultOption(String operation, String name) {
        if (defaults.containsKey(operation)) {
            Map options = defaults[operation]
            if (options.containsKey(name)) {
                return options[name]
            }
        }
        return null
    }
}
