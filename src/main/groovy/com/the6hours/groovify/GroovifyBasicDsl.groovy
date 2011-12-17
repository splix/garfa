package com.the6hours.groovify

import com.googlecode.objectify.Objectify
import com.googlecode.objectify.Key
import com.googlecode.objectify.Query
import org.slf4j.LoggerFactory
import org.slf4j.Logger

/**
 * TODO
 *
 * @since 17.08.11
 * @author Igor Artamonov (http://igorartamonov.com)
 */
class GroovifyBasicDsl {

    private static final Logger log = LoggerFactory.getLogger(this)

    void extend(Class dc) {
        def metaClass = dc.metaClass

        if (dc.declaredMethods.find { it.name == 'getKey' }) {
            log.info("Class $dc already contains .getKey() method")
        } else {
            metaClass.getKey = {->
                return new Key(dc, delegate.id)
            }
        }

        metaClass.save = {->
            def obj = delegate
            Holder.current.execute {
                put(obj)
            }
        }

        metaClass.'static'.update = { def id, Closure block ->
            def obj = delegate.get(id)
            obj.update(block)
        }

        metaClass.update = { Closure block ->
            def obj = delegate
            int tries = 3 //TODO make it configurable
            boolean saved = false
            Exception error = null
            Key key = obj.key
            def updated = null
            while (!saved && tries > 0) {
                saved = (Boolean)Holder.current.inTransaction {
                    tries--
                    Object stored = delegate.get(key)
                    block.delegate = stored
                    block.call(stored)
                    try {
                        delegate.put(stored)
                        updated = stored
                        return true
                    } catch (Exception e) {
                        error = e
                    }
                    return false
                }
            }
            if (!saved && error != null) {
                throw error
            }
            if (!saved) {
                return null
            }
            return updated
        }

        metaClass.'static'.get = { def id ->
            assert id != null
            assert id instanceof Key || id instanceof String || id instanceof Long
            if (id instanceof Key) {
                Holder.current.execute {
                    delegate.get(id)
                }
            } else {
                Holder.current.execute {
                    delegate.get(dc, id)
                }
            }
        }

        metaClass.'static'.load = { def id ->
            Key key = null
            if (id instanceof Key) {
                key = id
            } else {
                key = new Key(dc, id)
            }
            Holder.current.execute {
                delegate.find(key)
            }
        }

        metaClass.'static'.findAll = { Closure block ->
            Holder.current.execute {
                Objectify ob = delegate
                Query q = ob.query(dc)
                q.limit(1000)
                block.delegate = q
                block.call()
                return q.fetch().iterator().toList()
            }
        }

        metaClass.'static'.findFirst = { Closure block ->
            Holder.current.execute {
                Objectify ob = delegate
                Query q = ob.query(dc)
                block.delegate = q
                block.call()
                return q.get()
            }
        }
    }


}
