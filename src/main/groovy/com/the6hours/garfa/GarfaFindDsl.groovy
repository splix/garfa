package com.the6hours.garfa

import com.google.appengine.api.datastore.Cursor
import com.googlecode.objectify.Objectify
import com.googlecode.objectify.Query

/**
 *
 * @since 10.10.11
 * @author Igor Artamonov (http://igorartamonov.com)
 */
class GarfaFindDsl {

    void extend(Class dc) {
        def metaClass = dc.metaClass

        // DEPRECATED
        metaClass.'static'.iterWhere = { Map query ->
            return delegate.iterateWhere(query, [:], null)
        }
        metaClass.'static'.iterWhere = { Map query, Map params ->
            return delegate.iterateWhere(query, params, null)
        }
        metaClass.'static'.iterWhere = { Map query, Map params, Closure block ->
            return delegate.iterateWhere(query, params, block)
        }
        //DEPRECATED

        metaClass.'static'.iterateWhere = { Map query ->
            return delegate.iterateWhere(query, [:], null)
        }
        metaClass.'static'.iterateWhere = { Map query, Map params ->
            return delegate.iterateWhere(query, params, null)
        }
        metaClass.'static'.iterateWhere = { Map query, Map params, Closure block ->
            Holder.current.execute {
                Objectify ob = delegate
                Query q = ob.query(dc)
                if (params?.limit) {
                    q.limit(params.limit)
                }
                if (params?.order) {
                    q.order(params.order)
                }
                if (params?.cursor) {
                    Cursor cursor
                    if (params.cursor instanceof String) {
                        cursor = Cursor.fromWebSafeString(params.cursor)
                    } else if (params.cursor instanceof Cursor) {
                        cursor = params.cursor
                    } else {
                        cursor = Cursor.fromWebSafeString(params.cursor.toString())
                    }
                    q.startCursor(cursor)
                }
                query.entrySet().each { Map.Entry where ->
                    String field = where.key
                    if (field.indexOf(' ') < 0) {
                        field += ' ='
                    }
                    q.filter(field, where.value)
                }
                if (block) {
                    block.delegate = q
                    block.call()
                }
                return q.fetch().iterator()
            }
        }
        metaClass.'static'.findWhere = { Map query, Map params, Closure block ->
            return delegate.iterWhere(query, params, block).toList()
        }
        metaClass.'static'.findWhere = { Map query, Map params ->
            return delegate.findWhere(query, params, null)
        }
        metaClass.'static'.findWhere = { Map query ->
            return delegate.findWhere(query, null, null)
        }

        metaClass.'static'.findFirstWhere = { Map query, Map params, Closure block ->
            Iterator iter = delegate.iterWhere(query, params, block)
            if (iter.hasNext()) {
                return iter.next()
            }
            return null
        }
        metaClass.'static'.findFirstWhere = { Map query, Map params ->
            return delegate.findFirstWhere(query, params, null)
        }
        metaClass.'static'.findFirstWhere = { Map query ->
            return delegate.findFirstWhere(query, null, null)
        }

        metaClass.'static'.findByAncestor = { Object key ->
            return delegate.findByAncestor(key, null)
        }
        metaClass.'static'.findByAncestor = { Object key, Map params ->
            Holder.current.execute {
                Objectify ob = delegate
                Query q = ob.query(dc)
                if (params?.limit) {
                    q.limit(params.limit)
                }
                if (params?.order) {
                    q.order(params.order)
                }
                q.ancestor(key)
                return q.fetch().iterator().toList()
            }
        }
    }
}
