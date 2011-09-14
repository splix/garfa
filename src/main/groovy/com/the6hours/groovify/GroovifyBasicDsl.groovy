package com.the6hours.groovify

import com.googlecode.objectify.Objectify
import com.googlecode.objectify.Key
import com.googlecode.objectify.Query

/**
 * TODO
 *
 * @since 17.08.11
 * @author Igor Artamonov (http://igorartamonov.com)
 */
class GroovifyBasicDsl {

    void addCore(Class dc) {
        def metaClass = dc.metaClass

        metaClass.getKey = {->
            return new Key(dc, delegate.id)
        }

        metaClass.save = {->
            def obj = delegate
            Holder.current.execute {
                put(obj)
            }
        }

        metaClass.update = { Closure block ->
            def obj = delegate
            int tries = 3 //TODO configurable
            boolean saved = false
            Exception error = null
            Key key = obj.key
            while (!saved && tries > 0) {
                saved = (Boolean)Holder.current.inTransaction {
                    tries--
                    Object stored = delegate.get(key)
                    block.delegate = stored
                    block.call(stored)
                    try {
                        delegate.put(stored)
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

        metaClass.'static'.find = { def id ->
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
