package com.the6hours.garfa.testmodels

import javax.persistence.Id

/**
 * 
 * @author Igor Artamonov (http://igorartamonov.com)
 * @since 10.10.11
 */
class CarModel {

    static Map __forTesting = [
            beforeSave: 0,
            beforeInsert: 0,
            beforeUpdate: 0
    ]

    @Id
    Long id

    String vendor
    String model
    int year

    void beforeSave() {
        __forTesting.beforeSave++
    }

    void beforeInsert() {
        __forTesting.beforeInsert++
    }

    void beforeUpdate() {
        __forTesting.beforeUpdate++
    }
}
