package com.the6hours.groovify

import spock.lang.Specification
import com.googlecode.objectify.ObjectifyFactory
import com.the6hours.groovify.testmodels.CarModel
import com.googlecode.objectify.NotFoundException
import com.the6hours.groovify.testmodels.Car
import com.googlecode.objectify.Key

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
        [CarModel, Car].each {
             ofy.register(it)
             groovify.register(it)
        }
    }


    def "Save car"() {
        setup:
            CarModel car = new CarModel(
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
            CarModel car = new CarModel(
                    vendor: 'Vaz',
                    model: '2101'
            )
            car.save()
        when:
            CarModel car2 = CarModel.get(car.id)
        then:
            car2 != null
            car2.vendor == 'Vaz'
            car2.model == '2101'
        when:
            CarModel car3 = CarModel.get(car.id + 1)
        then:
            thrown(NotFoundException)
    }

    def "Get cars list"() {
        setup:
            CarModel car = new CarModel(
                    vendor: 'Vaz',
                    model: '2101'
            )
            car.save()
            CarModel car2 = new CarModel(
                    vendor: 'Vaz',
                    model: '2102'
            )
            car2.save()
            CarModel car3 = new CarModel(
                    vendor: 'Vaz',
                    model: '2105'
            )
            car3.save()
        when:
            List<CarModel> cars = CarModel.get([car.key, car2.key, car3.key])
        then:
            cars != null
            cars.size() == 3
            cars*.model == ['2101', '2102', '2105']
    }

    def "Load car"() {
        setup:
            CarModel car = new CarModel(
                    vendor: 'Vaz',
                    model: '2101'
            )
            car.save()
        when:
            CarModel car2 = CarModel.load(car.id)
        then:
            car2 != null
            car2.vendor == 'Vaz'
            car2.model == '2101'
        when:
            CarModel car3 = CarModel.load(car.id + 1)
        then:
            car3 == null
    }

    def "Load cars list"() {
        setup:
            CarModel car = new CarModel(
                    vendor: 'Vaz',
                    model: '2101'
            )
            car.save()
            CarModel car2 = new CarModel(
                    vendor: 'Vaz',
                    model: '2102'
            )
            car2.save()
            CarModel car3 = new CarModel(
                    vendor: 'Vaz',
                    model: '2105'
            )
            car3.save()
            car2.delete()
        when:
            List<CarModel> cars = CarModel.load([car.key, car2.key, car3.key])
        then:
            cars != null
            cars.size() == 3
            cars*.model == ['2101', null, '2105']
    }

    def "Update car"() {
        setup:
            CarModel car = new CarModel(
                    vendor: 'Vaz',
                    model: '2101'
            )
            car.save()
        when:
            CarModel car2 = CarModel.get(car.id)
            car2.update { CarModel curr ->
                curr.model = '2109'
            }
            CarModel car3 = CarModel.get(car.id)
        then:
            car3 != null
            car3.model == '2109'
    }

    def "Find first"() {
        setup:
            CarModel car = new CarModel(
                    vendor: 'Vaz',
                    model: '2101'
            )
            car.save()
            CarModel car2 = new CarModel(
                    vendor: 'Vaz',
                    model: '2109'
            )
            car2.save()
            CarModel car3 = new CarModel(
                    vendor: 'Ford',
                    model: 'Mustang'
            )
            car3.save()
        when:
            CarModel found = CarModel.findFirst { filter('vendor =', 'Vaz') }
        then:
            found != null
            found.id == car.id
    }

    def "Find all"() {
        setup:
            CarModel car = new CarModel(
                    vendor: 'Vaz',
                    model: '2101'
            )
            car.save()
            CarModel car2 = new CarModel(
                    vendor: 'Vaz',
                    model: '2109'
            )
            car2.save()
            CarModel car3 = new CarModel(
                    vendor: 'Ford',
                    model: 'Mustang'
            )
            car3.save()
        when:
            List found = CarModel.findAll { filter('vendor =', 'Vaz') }
        then:
            found != null
            found.size() == 2
            found.collect { it.id }.sort() == [car.id, car2.id].sort()
        when:
            found = CarModel.findAll { filter('vendor =', 'Ford') }
        then:
            found != null
            found.size() == 1
            found[0].id == car3.id
    }

    def "Create key for child model"() {
        setup:
            CarModel model = new CarModel(
                    vendor: 'Vaz',
                    model: '2101'
            )
            model.save()
            Car car1 = new Car(
                    model: model.key,
                    price: 100
            )
            car1.save()
            Car car2 = new Car(
                    model: model.key,
                    price: 120
            )
            car2.save()
        when:
            Key key1 = car1.key
            Key modelKey = model.key
        then:
            key1.kind == 'Car'
            key1.parent == modelKey
            modelKey.parent == null
    }
}
