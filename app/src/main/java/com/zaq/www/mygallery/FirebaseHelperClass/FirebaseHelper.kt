package com.zaq.www.mygallery.FirebaseHelperClass

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage

//firestore
val firestoreInstance: FirebaseFirestore
    get() {
        val firestore = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build()
        firestore.firestoreSettings = settings
        return firestore
    }
val picCollection
    get() = firestoreInstance.collection("pics")

//firebase storage
val storageInstance
    get() = FirebaseStorage.getInstance()
val storageRef
    get() = storageInstance.reference
val picDirectory
    get() = storageRef.child("pics")

//generate id
val newUUID
    get() = firestoreInstance.collection("Id").document().id