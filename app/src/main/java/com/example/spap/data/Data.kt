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

data class AddPlant(
    val email: String = "",
    val plantName: String = "",
    val plantType: String = "",
    val plantingDate: String = "",
    val plantWateringFrequency: String = "",
    val plantRepottedFrequency: String = "",
    val plantHumidity: String = "",
    val plantTemp: String = "",
    val plantMemo: String = "",
    val imageUrl: String? = null  // 이미지 URL이 선택적일 수 있음
)

data class PlantEvent(
    val planId: String = "",
    val pId: String = "",
    val uId: String = "",
    val type: String = "",
    val lastDate: String = "",
    val nextDate: String = "",
    val intervalDays: Int

)

data class CombinedPlantEvent(
    val imageUrl: String?,
    val plantName: String,
    val plantType: String,
    val taskType:String,
    val scheduleId: String,
    val intervalDays:Int
)