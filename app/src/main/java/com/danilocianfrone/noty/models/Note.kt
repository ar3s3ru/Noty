package com.danilocianfrone.noty.models

import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*

/**
 * Model class for Note object.
 *
 * A Note is defined as follows:
 *    * it has a text content
 *    * it has a priority
 *    * it can have an attached document
 *    * it has metadata for creation
 */
@RealmClass
open class Note : RealmObject() {

    @PrimaryKey var id: Long = -1

    @Index private var priorityVal: Int = Priority.MEDIUM.Value()         // Default Value to normal
    @Ignore var priority: Priority      = Priority.FromValue(priorityVal) // This is ignorated from Realm
        // When is set a new Value, update priorityVal Value as well
        set(value) { priorityVal = value.Value() }

    lateinit var content:  String
    lateinit var creation: Date       // Actual date
    var attach:   Document? = null    // Document can be nullable, 'cause it's not mandatory
}