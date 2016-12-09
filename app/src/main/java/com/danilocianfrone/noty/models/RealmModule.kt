package com.danilocianfrone.noty.models

import io.realm.annotations.RealmModule

@RealmModule(classes = arrayOf(Document::class, Note::class))
class RealmModule
