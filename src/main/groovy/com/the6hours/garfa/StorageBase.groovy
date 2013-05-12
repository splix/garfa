package com.the6hours.garfa

import com.googlecode.objectify.ObjectifyFactory
import com.googlecode.objectify.Objectify
import com.googlecode.objectify.Key
import com.googlecode.objectify.Query

import org.slf4j.LoggerFactory
import org.slf4j.Logger
import com.googlecode.objectify.NotFoundException

/**
 * TODO
 *
 * @since 13.07.11
 * @author Igor Artamonov (http://igorartamonov.com)
 */
class StorageBase<T> {

    private static final Logger log = LoggerFactory.getLogger(this)

    ObjectifyFactory objectifyFactory
    Class<T> targetClass


    StorageBase(Class<T> targetClass) {
        this.targetClass = targetClass
    }

    def query(Closure block) {
        Objectify ob = objectifyFactory.begin()
        return block.call(ob.query(targetClass))
    }

    List<T> list(Closure block) {
        Objectify ob = objectifyFactory.begin()
        Query<T> q = block.call(ob.query(targetClass))
        return q.fetch().iterator().toList()
    }

    T get(Closure block) {
        Objectify ob = objectifyFactory.begin()
        Query<T> q = block.call(ob.query(targetClass))
        List<T> res = q.fetch().iterator().toList()
        if (res.empty) {
            return null
        }
        return res.first()
    }

    def execute(Closure block) {
        Objectify ob = objectifyFactory.begin()
        return block.call(ob)
    }

    def inTransaction(Closure block) {
        Objectify ob = objectifyFactory.beginTransaction()
        try {
            def x = block.call(ob)
            ob.txn.commit()
            return x
        } catch (Exception e) {
            ob.txn.rollback()
            throw e
        }
    }

    def update(Key<T> key, Closure block) {
        return inTransaction { Objectify ob ->
            T obj = ob.get(key)
            def result = block.call(obj)
            ob.put(obj)
            return result
        }
    }

    Key<T> put(T obj) {
        return execute { Objectify ob ->
            ob.put(obj)
        }
    }

    List<T> getById(List<Key<T>> keys) {
        if (keys == null || keys.empty) {
            return Collections.emptyList()
        }
        return execute { Objectify ob ->
            return ob.get(keys).values() as List
        }
    }

    T getById(Key<T> key) {
        return execute { Objectify ob ->
            return ob.get(key)
        }
    }

    T getById(long id) {
        return execute { Objectify ob ->
            return ob.get(targetClass, id)
        }
    }

    T getById(String id) {
        return execute { Objectify ob ->
            return ob.get(targetClass, id)
        }
    }

    T loadById(long id) {
        return execute { Objectify ob ->
            try {
                return ob.get(targetClass, id)
            } catch (NotFoundException e) {
                StorageBase.log.warn("Entity with id $id not found")
            }
            return null
        }
    }

    T loadById(String id) {
        return execute { Objectify ob ->
            try {
                return ob.get(targetClass, id)
            } catch (NotFoundException e) {
                StorageBase.log.warn("Entity with name $id not found")
            }
            return null
        }
    }

}
