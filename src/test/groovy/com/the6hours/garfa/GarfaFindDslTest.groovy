package com.the6hours.garfa

import com.googlecode.objectify.ObjectifyFactory
import com.the6hours.garfa.testmodels.Car
import com.the6hours.spockappengine.WithGae
import spock.lang.Specification
import com.the6hours.garfa.testmodels.CarModel

/**
 * 
 * @author Igor Artamonov (http://igorartamonov.com)
 * @since 18.10.11
 */
@WithGae
class GarfaFindDslTest extends Specification {

    def setup() {
        ObjectifyFactory ofy = new ObjectifyFactory()
        Garfa garfa = new Garfa(ofy)
        [CarModel, Car].each {
             ofy.register(it)
             garfa.register(it)
        }

        ['2101', '2106', '2108', '2109'].eachWithIndex { String it, int idx ->
            CarModel car = new CarModel(
                    vendor: 'Vaz',
                    model: it,
                    year: 2000 + idx
            )
            car.save(flush: true)
        }
    }

    def "Basic find"() {
        when:
            List cars = CarModel.findWhere(['model': '2101'])
        then:
            cars != null
            cars.size() == 1
            cars[0].model == '2101'
    }

    def "Using limit"() {
        when:
            List cars = CarModel.findWhere(['vendor': 'Vaz'], [limit: 2])
        then:
            cars != null
            cars.size() == 2
    }

    def "Find greater than"() {
        when:
            List cars = CarModel.findWhere(['year >': 2002])
        then:
            cars != null
            cars.size() == 1
            cars[0].model == '2109'
    }

    def "Find greater or equal than"() {
        when:
            List cars = CarModel.findWhere(['year >=': 2002])
        then:
            cars != null
            cars.size() == 2
            cars[0].model == '2108'
            cars[1].model == '2109'
    }

    def "Find greater or equal than w order"() {
        when:
            List cars = CarModel.findWhere(['year >=': 2002], [order: '-year'])
        then:
            cars != null
            cars.size() == 2
            cars[0].model == '2109'
            cars[1].model == '2108'
        when:
            cars = CarModel.findWhere(['year >=': 2002], [order: 'year'])
        then:
            cars != null
            cars.size() == 2
            cars[0].model == '2108'
            cars[1].model == '2109'
    }
}
