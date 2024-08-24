package com.example.spap.home.plantmanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spap.data.Plant
import com.google.firebase.firestore.FirebaseFirestore

class PlantManagerViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val _plantInfo = MutableLiveData<List<Plant>>()
    val plantInfo: LiveData<List<Plant>> = _plantInfo

    fun plantForUser(email: String) {
        firestore.collection("plants")
            .whereEqualTo("email", email)
            .addSnapshotListener { plantDocuments, exception ->
                if (exception != null) {
                    return@addSnapshotListener
                }
                if (plantDocuments != null) {
                    val plants = plantDocuments.documents.mapNotNull { document ->
                        document.toObject(Plant::class.java)
                    }
                    _plantInfo.value = plants
                }
            }
    }
}