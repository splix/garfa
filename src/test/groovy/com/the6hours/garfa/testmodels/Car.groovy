package com.the6hours.garfa.testmodels

import com.googlecode.objectify.annotation.Parent
import javax.persistence.Id
import com.googlecode.objectify.Key

/**
 * 
 * @author Igor Artamonov (http://igorartamonov.com)
 * @since 20.05.12
 */
class Car {

    @Id
    Long id
    @Parent
    Key<CarModel> model

    int price
    String color

}
