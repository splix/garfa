package com.the6hours.groovify

import com.the6hours.groovify.testmodels.Car
import com.googlecode.objectify.ObjectifyFactory
import spock.lang.Specification

/**
 * 
 * @author Igor Artamonov (http://igorartamonov.com)
 * @since 18.10.11
 */
@WithGae
class GroovifyFindDslTest extends Specification {

    def setup() {
        ObjectifyFactory ofy = new ObjectifyFactory()
        Groovify groovify = new Groovify(
                objectifyFactory: ofy
        )
        [Car].each {
             ofy.register(it)
             groovify.register(it)
        }

        ['2101', '2106', '2108'].each {
            Car car = new Car(
                    vendor: 'Vaz',
                    model: it
            )
            car.save()
        }
    }

    def "Basic find"() {
        when:
            List cars = Car.findWhere(['model': '2101'])
        then:
            cars != null
            cars.size() == 1
            cars[0].model == '2101'
    }

    def "Using limit"() {
        when:
            List cars = Car.findWhere(['vendor': 'Vaz'], [limit: 2])
        then:
            cars != null
            cars.size() == 2
    }


}
