package com.example.passportgeneration.models

import android.media.Image
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class MyPerson {
    @PrimaryKey(autoGenerate = true)
    var id:Int? = null

    var name:String? = null
    var seriyaPassport:String? = null
    var image:String? = null
    var position:Int? = null
    constructor(id: Int?, name: String?, seriyaPassport: String?,image: String?,position:Int?) {
        this.id = id
        this.name = name
        this.seriyaPassport = seriyaPassport
        this.image = image
        this.position = position
    }

    constructor(name: String?, seriyaPassport: String?,image: String?,position: Int?) {
        this.name = name
        this.seriyaPassport = seriyaPassport
        this.image = image
        this.position = position
    }

    constructor()


}