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

    }
}
