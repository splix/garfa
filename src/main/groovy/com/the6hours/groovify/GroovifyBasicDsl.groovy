package com.the6hours.groovify

import com.googlecode.objectify.Objectify
import com.googlecode.objectify.Key
import com.googlecode.objectify.Query
import org.slf4j.LoggerFactory
import org.slf4j.Logger
import java.lang.reflect.Field
import com.googlecode.objectify.annotation.Parent

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
            Field parent = dc.declaredFields.find { f ->
                f.declaredAnnotations.find { a ->
                    a.annotationType() == Parent
                } != null
            }
            if (parent) {
                String parentField = parent.name
                metaClass.getKey = {->
                    return new Key(delegate.getAt(parentField), dc, delegate.id)
                }
            } else {
                metaClass.getKey = {->
                    return new Key(dc, delegate.id)
                }
            }
        }

        metaClass.save = {->
            def obj = delegate
            Holder.current.execute {
                put(obj)
            }
        }

        metaClass.delete = {->
            def obj = delegate
            Holder.current.execute {
                delete(obj)
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
                tries--
                saved = (Boolean)Holder.current.inTransaction {
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
            assert id != null : "ID cannot be null"
            assert id instanceof Key || id instanceof String || id instanceof Long || id instanceof Iterable
            if (id instanceof Key) {
                Holder.current.execute {
                    delegate.get(id)
                }
            } else if (id instanceof Iterable) {
                Map loaded = Holder.current.execute {
                    delegate.get(id)
                }
                return id.collect { loaded[it] }
            } else {
                Holder.current.execute {
                    delegate.get(dc, id)
                }
            }
        }

        metaClass.'static'.load = { def id ->
            if (id == null) {
                return null
            }
            assert id instanceof Key || id instanceof String || id instanceof Long || id instanceof Iterable
            if (id instanceof Key) {
                Holder.current.execute {
                    delegate.find(id)
                }
            } else if (id instanceof Iterable) {
                return delegate.get(id)
            } else {
                Holder.current.execute {
                    delegate.find(dc, id)
                }
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
