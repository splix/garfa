package com.the6hours.groovify

import com.googlecode.objectify.ObjectifyFactory

/**
 * TODO
 *
 * @since 28.08.11
 * @author Igor Artamonov (http://igorartamonov.com)
 */
class Groovify {

    GroovifyBasicDsl basicDsl = new GroovifyBasicDsl()
   GroovifyFindDsl  findDsl = new GroovifyFindDsl()

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
