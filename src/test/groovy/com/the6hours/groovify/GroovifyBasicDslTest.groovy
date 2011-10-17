package com.the6hours.groovify

import spock.lang.Specification
import com.googlecode.objectify.ObjectifyFactory
import com.the6hours.groovify.testmodels.Car
import com.googlecode.objectify.NotFoundException

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

    def "Get car"() {
        setup:
            Car car = new Car(
                    vendor: 'Vaz',
                    model: '2101'
            )
            car.save()
        when:
            Car car2 = Car.get(car.id)
        then:
            car2 != null
            car2.vendor == 'Vaz'
            car2.model == '2101'
        when:
            Car car3 = Car.get(car.id + 1)
        then:
            thrown(NotFoundException)
    }

    def "Load car"() {
        setup:
            Car car = new Car(
                    vendor: 'Vaz',
                    model: '2101'
            )
            car.save()
        when:
            Car car2 = Car.load(car.id)
        then:
            car2 != null
            car2.vendor == 'Vaz'
            car2.model == '2101'
        when:
            Car car3 = Car.load(car.id + 1)
        then:
            car3 == null
    }
}
