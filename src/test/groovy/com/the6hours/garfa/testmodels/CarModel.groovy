package com.the6hours.garfa.testmodels

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index


/**
 * 
 * @author Igor Artamonov (http://igorartamonov.com)
 * @since 10.10.11
 */
@Entity
class CarModel {

    static Map __forTesting = [
            beforeSave: 0,
            beforeInsert: 0,
            beforeUpdate: 0
    ]

    @Id
    Long id

    @Index
    String vendor
    @Index
    String model
    @Index
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
