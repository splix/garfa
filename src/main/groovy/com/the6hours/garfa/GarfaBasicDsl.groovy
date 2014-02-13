package com.the6hours.garfa

import com.googlecode.objectify.Objectify
import com.googlecode.objectify.Key
import com.googlecode.objectify.cmd.LoadType
import com.googlecode.objectify.cmd.Query
import com.googlecode.objectify.cmd.QueryExecute
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
class GarfaBasicDsl {

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
                    Key parentKey = delegate.getAt(parentField)
                    return Key.create(parentKey, dc, delegate.id)
                }
            } else {
                metaClass.getKey = {->
                    return Key.create(dc, delegate.id)
                }
            }
        }

        metaClass.'static'.withObjectify = { Closure block ->
            Holder.current.execute(block)
        }

        metaClass.save = { Map opts = [:] ->
            def obj = delegate
            processBeforeXXX(obj)
            opts = opts ?: [:]
            if (opts.flush == null) {
                opts.flush = Garfa.defaultOption('save', 'flush')
            }
            Holder.current.execute {
                Objectify ob = delegate
                if (opts?.flush) {
                    ob.save().entity(obj).now()
                } else {
                    ob.save().entity(obj)
                }
            }
        }

        metaClass.delete = { Map opts = [:] ->
            def obj = delegate
            opts = opts ?: [:]
            if (opts.flush == null) {
                opts.flush = Garfa.defaultOption('delete', 'flush')
            }
            Holder.current.execute {
                Objectify ob = delegate
                if (opts?.flush) {
                    ob.delete().entity(obj).now()
                } else {
                    ob.delete().entity(obj)
                }
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
                saved = (Boolean)Holder.current.withTransaction {
                    Objectify ob = delegate
                    Object stored = ob.load().key(key).now()
                    block.delegate = stored
                    block.call(stored)
                    try {
                        processBeforeXXX(stored)
                        ob.save().entity(stored)
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

        metaClass.getChild = { Class clazz, def id ->
            assert id != null : "ID cannot be null"
            assert id instanceof String || id instanceof Long
            Key key = Key.create(delegate.getKey(), clazz, id)
            delegate.get(key)
        }

        metaClass.loadChild = { Class clazz, def id ->
            assert id != null : "ID cannot be null"
            assert id instanceof String || id instanceof Long
            Key key = Key.create(delegate.getKey(), clazz, id)
            delegate.get(key, [safe: false])
        }

        metaClass.'static'.getAll = { def ids ->
            assert ids != null : "ID list cannot be null"
            assert ids instanceof Iterable
            return delegate.get(ids)
        }

        metaClass.'static'.get = { def id, Map opts = [:] ->
            assert id != null : "ID cannot be null"
            assert id instanceof Key || id instanceof String || id instanceof Long || id instanceof Integer || id instanceof Iterable
            if (id instanceof Integer) {
                id = id.longValue()
            }
            opts = opts ?: [:]
            if (opts.safe == null) {
                opts.safe = Garfa.defaultOption('get', 'safe')
            }
            if (id instanceof Key) {
                Holder.current.execute {
                    def loader = delegate.load().key(id)
                    if (opts.safe) {
                        return loader.safe()
                    } else {
                        return loader.now()
                    }
                }
            } else if (id instanceof Iterable) {
                Map loaded = Holder.current.execute {
                    delegate.load().keys(id)
                }
                return id.collect { loaded[it] }
            } else {
                Holder.current.execute {
                    def loader = delegate.load().type(dc).id(id)
                    if (opts?.safe) {
                        return loader.safe()
                    } else {
                        return loader.now()
                    }
                }
            }
        }

        metaClass.'static'.load = { def id ->
            delegate.get(id, [safe: false])
        }

        metaClass.'static'.findAll = { Closure block ->
            Holder.current.execute {
                Objectify ob = delegate
                Query q = ob.load().type(dc)
                q = q.limit(1000)
                block.delegate = q
                def q2 = block.call()
                if (q2 != null && q2 instanceof QueryExecute) {
                    return q2.list()
                }
                return q.list()
            }
        }

        metaClass.'static'.findFirst = { Closure block ->
            Holder.current.execute {
                Objectify ob = delegate
                Query q = ob.load().type(dc)
                block.delegate = q
                def q2 = block.call()
                if (q2 != null && q2 instanceof QueryExecute) {
                    return q2.first().now()
                }
                return q.first().now()
            }
        }
    }


    void processBeforeXXX(def obj) {
        if (obj == null) {
            return
        }
        if (obj.respondsTo('beforeSave')) {
            obj.beforeSave()
        }
        if (obj.id == null) {
            if (obj.respondsTo('beforeInsert')) {
                obj.beforeInsert()
            }
        } else {
            if (obj.respondsTo('beforeUpdate')) {
                obj.beforeUpdate()
            }
        }
    }
}
