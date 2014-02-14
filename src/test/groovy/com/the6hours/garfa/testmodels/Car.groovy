package com.the6hours.garfa.testmodels

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.googlecode.objectify.annotation.Parent
import com.googlecode.objectify.Key

/**
 * 
 * @author Igor Artamonov (http://igorartamonov.com)
 * @since 20.05.12
 */
@Entity
class Car {

    @Id
    Long id
    @Parent
    Key<CarModel> model

    @Index
    int price
    @Index
    String color

}
