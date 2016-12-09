package com.danilocianfrone.noty.models

import android.net.Uri
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.RealmClass

/**
 * Model class for a Document object.
 *
 * A Document model is structured with:
 *     * a description
 *     * an URI path for the ContentResolver
 *     * an optional title
 */
@RealmClass
open class Document : RealmObject() {
    var title:       String? = null
    var description: String  = "<no description>"
    var uriString:   String  = ""
}