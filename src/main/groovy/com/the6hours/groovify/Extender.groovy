package com.the6hours.groovify

import java.lang.reflect.Field
import java.lang.reflect.Modifier
import com.googlecode.objectify.Objectify

/**
 * TODO
 *
 * @since 13.07.11
 * @author Igor Artamonov (http://igorartamonov.com)
 */
class Extender {

    void extend(Class clazz) {
        Field modelField = clazz.getField("model")
        if (modelField == null || !Modifier.isStatic(modelField.modifiers)) {
            return
        }
        Class modelClass = modelField.get(null)

        clazz.metaClass.findAll = { Closure code ->
            Objectify ob = this.begin()
            code.call(ob)
        }

        clazz.metaClass.update = { Object id, Closure code ->
            Objectify ob = this.beginTransaction()
            try {
                def obj = ob.get(modelClass, id)
                code.call(obj)
                ob.txn.commit()
            } catch (Exception e) {
                ob.txn.rollback()
                throw e
            }
        }

        clazz.metaClass.findAllWhere = { def where ->
            Objectify ob = this.begin()
            code.call(ob)
        }
    }
}
