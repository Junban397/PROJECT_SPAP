package com.example.spap.Home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spap.data.CombinedPlantEvent
import com.example.spap.data.Plant
import com.example.spap.data.PlantEvent
import java.time.LocalDate

class PlantEventViewModel : ViewModel() {
    private val _combinedPlantEvents = MutableLiveData<List<CombinedPlantEvent>>()
    val combinedPlantEvents: LiveData<List<CombinedPlantEvent>> = _combinedPlantEvents

    private var currentUserId: String = ""


    fun loadPlantEvents(uId:String){
        currentUserId = uId
        val plantEvents = getPlantEventsFromFirebase(uId)
        val plants = getPlantsFromFirebase(uId)

        val today = LocalDate.now()  // 현재 날짜만 가져옴

        val todayEvents = plantEvents.filter { event ->
            event.date == today  // 날짜 비교
        }

        val combinedEvents = todayEvents.map { event ->
            val plant = plants[event.pId]
            CombinedPlantEvent(
                plantEvent = event,
                plantImage = plant?.pImg,
                plantName = plant?.pName ?: "",
                plantType = plant?.pType ?: ""
            )
        }

        _combinedPlantEvents.value = combinedEvents
    }
    private fun getPlantEventsFromFirebase(userId: String): List<PlantEvent> {
        // Firebase에서 사용자의 PlantEvent 목록을 가져오는 함수 (가상의 함수)
        // 이 함수는 비동기적으로 Firebase에서 데이터를 가져와야 함
        return listOf() // 여기에 Firebase 데이터를 가져오는 로직을 구현
    }

    private fun getPlantsFromFirebase(userId: String): Map<String, Plant> {
        // Firebase에서 사용자의 Plant 목록을 가져오는 함수 (가상의 함수)
        // 이 함수는 비동기적으로 Firebase에서 데이터를 가져와야 함
        return mapOf() // 여기에 Firebase 데이터를 가져오는 로직을 구현
    }
}