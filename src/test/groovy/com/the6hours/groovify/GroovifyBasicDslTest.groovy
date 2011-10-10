package com.the6hours.groovify

import spock.lang.Specification
import com.googlecode.objectify.ObjectifyFactory
import com.the6hours.groovify.testmodels.Car

/**
 * 
 * @author Igor Artamonov (http://igorartamonov.com)
 * @since 10.10.11
 */
@WithGae
class GroovifyBasicDslTest extends Specification {

    def setup() {
        ObjectifyFactory ofy = new ObjectifyFactory()
        Groovify groovify = new Groovify(
                objectifyFactory: ofy
        )
        [Car].each {
             ofy.register(it)
             groovify.register(it)
        }
    }


    def "Save car"() {
        setup:
            Car car = new Car(
                    vendor: 'Vaz',
                    model: '2101'
            )
        when:
            car.save()
        then:
            car.id != null

    }
}
