package com.the6hours.garfa

import com.googlecode.objectify.ObjectifyFactory
import com.the6hours.garfa.testmodels.Car
import com.the6hours.garfa.testmodels.CarModel
import com.the6hours.spockappengine.WithGae
import spock.lang.Specification

/**
 *
 * Since 13.05.13
 * @author Igor Artamonov, http://igorartamonov.com
 */
@WithGae
class GarfaDynamicFindDslTest extends Specification {

    def setup() {
        ObjectifyFactory ofy = new ObjectifyFactory()
        Garfa garfa = new Garfa(ofy)
        [CarModel, Car].each {
             ofy.register(it)
             garfa.register(it)
        }

    }

    def "Parse one field"() {
        setup:
        GarfaDynamicFindDsl dsl = new GarfaDynamicFindDsl()
        when:
        def act = dsl.parseTail('Name')
        then:
        act == ['name']
    }

    def "Parse two fields"() {
        setup:
        GarfaDynamicFindDsl dsl = new GarfaDynamicFindDsl()
        when:
        def act = dsl.parseTail('NameAndPassword')
        then:
        act == ['name', 'password']
    }

    def "Parse two long fields"() {
        setup:
        GarfaDynamicFindDsl dsl = new GarfaDynamicFindDsl()
        when:
        def act = dsl.parseTail('NameAndFullName')
        then:
        act == ['name', 'fullName']
    }

    def "Parse fields containing and"() {
        setup:
        GarfaDynamicFindDsl dsl = new GarfaDynamicFindDsl()
        when:
        def act = dsl.parseTail('AndrewAndCommand')
        then:
        act == ['andrew', 'command']
    }

    def "Parse unusual fields"() {
        setup:
        GarfaDynamicFindDsl dsl = new GarfaDynamicFindDsl()
        when:
        def act = dsl.parseTail('XAndYAndZ')
        then:
        act == ['x', 'y', 'z']
        when:
        act = dsl.parseTail('X')
        then:
        act == ['x']
        when:
        act = dsl.parseTail('I18nAndA16z')
        then:
        act == ['i18n', 'a16z']
    }

    def "Build where for one field"() {
        setup:
        GarfaDynamicFindDsl dsl = new GarfaDynamicFindDsl()
        when:
        def act = dsl.buildWhere(['name'], ['John'])
        then:
        act == [
                name: 'John'
        ]
    }

    def "Build where for few fields"() {
        setup:
        GarfaDynamicFindDsl dsl = new GarfaDynamicFindDsl()
        when:
        def act = dsl.buildWhere(['name', 'age'], ['John', 23])
        then:
        act == [
                name: 'John',
                age: 23
        ]
        when:
        act = dsl.buildWhere(['name', 'age'], ['John', 23, true])
        then:
        act == [
                name: 'John',
                age: 23
        ]
        when:
        act = dsl.buildWhere(['name', 'age', 'email'], ['John', 23])
        then:
        act == [
                name: 'John',
                age: 23
        ]
    }

    def "Get extra"() {
        setup:
        GarfaDynamicFindDsl dsl = new GarfaDynamicFindDsl()
        when:
        def act = dsl.getExtra(['name'], ['John'])
        then:
        act.empty
        when:
        act = dsl.getExtra(['name'], ['John', [limit: 4]])
        then:
        act == [
                [limit: 4]
        ]
        when:
        act = dsl.getExtra(['name'], ['John', [limit: 4], { -> println "Hi!"}])
        then:
        act.size() == 2
        act[0] == [limit: 4]
        act[1] instanceof Closure
        when:
        act = dsl.getExtra(['name', 'zip'], ['John', 10032])
        then:
        act.empty
        when:
        act = dsl.getExtra(['name', 'zip'], ['John', 10032, [limit: 4]])
        then:
        act == [
                [limit: 4]
        ]
        when:
        act = dsl.getExtra(['name', 'zip'], ['John', 10032, [limit: 4], { -> println "Hi!"}])
        then:
        act.size() == 2
        act[0] == [limit: 4]
        act[1] instanceof Closure
    }

    def "Load cars list"() {
        setup:
            CarModel car = new CarModel(
                    vendor: 'Vaz',
                    model: '2101'
            )
            car.save()
            CarModel car2 = new CarModel(
                    vendor: 'Gaz',
                    model: '3309'
            )
            car2.save()
            CarModel car3 = new CarModel(
                    vendor: 'Vaz',
                    model: '2105'
            )
            car3.save()
            CarModel car4 = new CarModel(
                    vendor: 'Gaz',
                    model: '3309'
            )
            car4.save()
        when:
            List<CarModel> cars = CarModel.findByModel('2105')
        then:
            cars != null
            cars.size() == 1
            cars*.model == ['2105']
        when:
            cars = CarModel.findByVendor('Vaz')
        then:
            cars != null
            cars.size() == 2
            cars*.model == ['2101', '2105']
        when:
            def act = CarModel.findFirstByVendor('Vaz')
        then:
            act != null
            act.model == '2101'
        when:
            cars = CarModel.findByVendorAndModel('Vaz', '2105')
        then:
            cars != null
            cars.size() == 1
            cars*.model == ['2105']
        when:
            cars = CarModel.findByVendorAndModel('Gaz', '3309', [limit: 1])
        then:
            cars != null
            cars.size() == 1
            cars*.model == ['3309']
    }
}

