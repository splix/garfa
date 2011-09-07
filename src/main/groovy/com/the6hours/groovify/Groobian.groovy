package com.the6hours.groovify

import com.googlecode.objectify.ObjectifyFactory

/**
 * TODO
 *
 * @since 28.08.11
 * @author Igor Artamonov (http://igorartamonov.com)
 */
class Groobian {

    void setModels(List<Class> models) {
        GroobianBasicDsl basicDsl = new GroobianBasicDsl()
        models.each {
            basicDsl.addCore(it)
        }
    }

    void setObjectifyFactory(ObjectifyFactory ofy) {
        Holder._objectifyFactory = ofy
    }
}
