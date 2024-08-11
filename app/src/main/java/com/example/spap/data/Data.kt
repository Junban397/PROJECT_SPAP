package com.example.spap.data

import java.time.LocalDate
import java.util.Date

data class UserData(
    val uId: String = "",
    var email: String = "",
    var password: String = "",
    var name: String = "",
    var dateOfBirth: String? = ""
)

data class Plant(
    val pId: String = "",
    val uId: String = "",
    val pName: String = "",
    val pType: String = "",
    val adoptedDate: LocalDate? = null,
    val wateringFrequencyRange: String? = null,
    val lastWatered: LocalDate? = null,
    val fertilizerFrequencyRange: String? = null,
    val notes: String? = null,
    val humidity: String? = null,
    val temperature: String? = null,
    val pImg: String? = null
)

data class PlantEvent(
    val planId: String = "",
    val pId: String = "",
    val uId: String = "",
    val title: String = "",
    val date: LocalDate = LocalDate.now(),
    val endDate: LocalDate? = null

)

data class CombinedPlantEvent(
    val plantEvent: PlantEvent,
    val plantImage: String?,
    val plantName: String,
    val plantType: String
)