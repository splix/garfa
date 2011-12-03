package com.the6hours.groovify

import com.googlecode.objectify.Objectify
import com.googlecode.objectify.Query

/**
 *
 * @since 10.10.11
 * @author Igor Artamonov (http://igorartamonov.com)
 */
class GroovifyFindDsl {

    void extend(Class dc) {
        def metaClass = dc.metaClass
        metaClass.'static'.iterWhere = { Map query, Map params, Closure block ->
            Holder.current.execute {
                Objectify ob = delegate
                Query q = ob.query(dc)
                if (params?.limit) {
                    q.limit(params.limit)
                }
                if (params?.order) {
                    q.order(params.order)
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
    }
}
