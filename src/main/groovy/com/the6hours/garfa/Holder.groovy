package com.the6hours.garfa

import com.googlecode.objectify.Objectify
import com.googlecode.objectify.ObjectifyFactory
import com.googlecode.objectify.ObjectifyService
import com.googlecode.objectify.Work

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
            ObjectifyService.factory = _objectifyFactory
        }
        return h
    }

    // **********************************************

    def execute(Closure block) {
        Objectify ob = ObjectifyService.ofy()
        block.delegate = ob
        return block.call()
    }

    def withTransaction(Closure block) {
        Objectify ob = ObjectifyService.ofy()
        Work work = new Work() {
            @Override
            Object run() {
                block.delegate = ObjectifyService.ofy()
                return block.call()
            }
        }
        return ob.transact(work)
    }


}
