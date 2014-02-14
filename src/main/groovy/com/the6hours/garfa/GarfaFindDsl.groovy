package com.the6hours.garfa

import com.google.appengine.api.datastore.Cursor
import com.google.appengine.api.datastore.QueryResultIterable
import com.googlecode.objectify.Objectify
import com.googlecode.objectify.cmd.Query

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

        metaClass.'static'.all = {
            return delegate.findWhere([:], [:], null)
        }

        metaClass.'static'.queryWhere = { Map query, Map params ->
            Holder.current.execute {
                Objectify ob = delegate
                Query q = ob.load().type(dc)
                if (params?.limit) {
                    q = q.limit(params.limit as Integer)
                }
                if (params?.offset) {
                    q = q.offset(params.offset as Integer)
                }
                if (params?.order) {
                    q = q.order(params.order)
                } else  if (params?.sort) {
                    q = q.order(params.sort)
                }
                if (params?.ancestor) {
                    q = q.ancestor(params.ancestor)
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
                    q = q.startAt(cursor)
                }
                query.entrySet().each { Map.Entry where ->
                    String field = where.key
                    if (field == 'ancestor') {
                        q = q.ancestor(where.value)
                    } else {
                        if (field.indexOf(' ') < 0) {
                            //just a fields name, means 'equal to'
                            //'field' -> 'field ='
                            field += ' ='
                        }
                        q = q.filter(field, where.value)
                    }
                }
                return q
            }
        }

        metaClass.'static'.iterateWhere = { Map query ->
            return delegate.iterateWhere(query, [:], null)
        }
        metaClass.'static'.iterateWhere = { Map query, Map params ->
            return delegate.iterateWhere(query, params, null)
        }
        metaClass.'static'.iterateWhere = { Map query, Map params, Closure block ->
            Query q = delegate.queryWhere(query, params)
            if (block) {
                block.delegate = q
                block.call()
            }
            return q.iterator()
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
            Iterator iter = delegate.iterateWhere(query, params, block)
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
            return delegate.iterateByAncestor(key, params).toList()
        }

        metaClass.'static'.iterateByAncestor = { Object key ->
            return delegate.iterateByAncestor(key, null)
        }
        metaClass.'static'.iterateByAncestor = { Object key, Map params ->
            return delegate.iterateByAncestor(key, null, null)
        }
        metaClass.'static'.iterateByAncestor = { Object key, Closure block ->
            return delegate.iterateByAncestor(key, null, block)
        }

        metaClass.'static'.iterateByAncestor = { Object key, Map params, Closure block ->
            Holder.current.execute {
                Objectify ob = delegate
                Query q = ob.load().type(dc)
                if (params?.limit) {
                    q = q.limit(params.limit)
                }
                if (params?.order) {
                    q = q.order(params.order)
                }
                q = q.ancestor(key)
                if (block) {
                    block.delegate = q
                    def q2 = block.call()
                    if (q2 && q2 instanceof QueryResultIterable) {
                        return q2.iterator()
                    }
                }
                return q.iterator()
            }
        }
    }
}
