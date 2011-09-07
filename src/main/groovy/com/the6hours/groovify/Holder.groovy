package com.the6hours.groovify

import com.googlecode.objectify.Objectify
import com.googlecode.objectify.ObjectifyFactory

/**
 * TODO
 *
 * @since 17.08.11
 * @author Igor Artamonov (http://igorartamonov.com)
 */
class Holder {

    private static ThreadLocal<Holder> holder = new ThreadLocal<Holder>()

    ObjectifyFactory objectifyFactory

    static ObjectifyFactory _objectifyFactory

    static Holder getCurrent() {
        Holder h = holder.get()
        if (h == null) {
            h = new Holder(
                    objectifyFactory: _objectifyFactory
            )
            holder.set(h)
        }
        return h
    }

    // **********************************************

    def execute(Closure block) {
        Objectify ob = objectifyFactory.begin()
        block.delegate = ob
        return block.call()
    }

    def inTransaction(Closure block) {
        Objectify ob = objectifyFactory.beginTransaction()
        block.delegate = ob
        try {
            def x = block.call()
            ob.txn.commit()
            return x
        } catch (Exception e) {
            ob.txn.rollback()
            throw e
        }
    }


}
