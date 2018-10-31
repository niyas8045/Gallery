package com.zaq.www.mygallery.Data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.FieldValue
import java.util.*

class Pic {
    var imagePath: String? = null
    var timestamp: Timestamp? = null

    constructor()
    constructor(name: String?,
                timestamp: Timestamp? = null) {
        this.imagePath = name
        this.timestamp = timestamp
    }

    @Exclude
    fun toMap(): HashMap<String, Any?> {
        val result = HashMap<String, Any?>()
        result["imagePath"] = imagePath
        result["timestamp"] = FieldValue.serverTimestamp()
        return result
    }
}