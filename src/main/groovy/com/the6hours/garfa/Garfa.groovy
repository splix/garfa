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
    }

    void setObjectifyFactory(ObjectifyFactory ofy) {
        Holder._objectifyFactory = ofy
    }
}
