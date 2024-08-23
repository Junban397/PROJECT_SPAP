package com.example.spap.home.tasks

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spap.data.CombinedPlantEvent
import com.example.spap.data.PlantEvent
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class PlantEventViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    //MutableLiveData 데이터의 변화를 관찰하는 객체
    private val _schedules = MutableLiveData<List<CombinedPlantEvent>>()
    //LiveData는 관찰만 하기 때문에 따로
    val schedules: LiveData<List<CombinedPlantEvent>> = _schedules

    //오늘 스케줄 불러오는 함수
    fun schedulesForToday(email: String) {
        val todayDate = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date())

        //whereEqyalTo 특정 필드가 값과 같은 문서 필터
        firestore.collection("schedules")
            .whereEqualTo("email", email)
            .whereEqualTo("nextDate", todayDate)
            //addSnapshotListener파이어베이스 데이터베이스 변경을 실시간 감지 -> 데이터가 바뀌면 콜백 실행
            // exception 예외 처리
            .addSnapshotListener { scheduleDocuments, exception ->
                if (exception != null) {
                    return@addSnapshotListener
                }

                if (scheduleDocuments != null) {
                    val plantIds = scheduleDocuments.documents.mapNotNull { it.getString("plantId") }   //plantId를 추출 후 리스트로

                    if (plantIds.isNotEmpty()) {
                        // Plant documents 가져오기
                        val plantFetchTasks = plantIds.map { id ->
                            firestore.collection("plants").document(id).get()
                        }

                        Tasks.whenAllSuccess<DocumentSnapshot>(plantFetchTasks) //작업 성공 시 처리하는 콜백
                            .addOnSuccessListener { plantDocuments ->
                                if (plantDocuments.isEmpty()) {
                                    Log.d("PlantEventViewModel", "No plant documents found for plant IDs: $plantIds")
                                }

                                // Schedule documents와 Plant documents를 조합
                                val combinedList = combinePlantAndSchedule(plantDocuments, scheduleDocuments)
                                _schedules.value = combinedList
                            }
                            .addOnFailureListener { exception ->
                                Log.e("PlantEventViewModel", "Failed to fetch plant documents", exception)
                            }
                    } else {
                        _schedules.value = emptyList()
                    }
                } else {    //리사이클러뷰에 오늘 할 일 완료 됬다고 띄우기
                    _schedules.value = emptyList()
                }
            }
    }
    //식물, 스케줄 데이터를 결합하여 List생성
    private fun combinePlantAndSchedule(
        plantDocuments: List<DocumentSnapshot>,
        scheduleDocuments: QuerySnapshot
    ): List<CombinedPlantEvent> {
        val plantMap = plantDocuments.associateBy { it.id }  //associateBy 리스트를 키, 값으로 구성된 맵으로 변환
        val combinedList = mutableListOf<CombinedPlantEvent>()

        for (schedule in scheduleDocuments) {
            //스케줄 데이터에서 plantId를 얻어옴
            //얻은plantId를 통해 식물 문서를 가져옴
            val plantId = schedule.getString("plantId") ?: continue
            val plantDoc = plantMap[plantId] ?: continue

            val imageUrl = plantDoc.getString("imageUrl")
            val plantName = plantDoc.getString("plantName") ?: ""
            val plantType = plantDoc.getString("plantType") ?: ""
            val taskType = schedule.getString("type") ?: ""
            val scheduleId = schedule.id
            val intervalDays = schedule.getLong("intervalDays")?.toInt()?:0

            combinedList.add(CombinedPlantEvent(imageUrl, plantName, plantType, taskType, scheduleId,intervalDays))
        }
        return combinedList
    }

    //스케줄 완료시 데이터 업데이트
    fun completeTask(scheduleId: String, intervalDays: Int) {
        val todayDate = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date())

        firestore.collection("schedules").document(scheduleId)
            .update(
                mapOf(
                    "lastDate" to todayDate,
                    "nextDate" to nextDate(todayDate, intervalDays)
                )
            )
            .addOnSuccessListener {
                Log.d("PlantEventViewModel", "Task completed successfully for scheduleId: $scheduleId")
            }
            .addOnFailureListener { exception ->
                Log.e("PlantEventViewModel", "Failed to complete task for scheduleId: $scheduleId", exception)
            }
    }
    //다음 스케줄 업데이트를 위한 날짜 변경 함수
    private fun nextDate(lastDate: String, intervalDays: Int): String {
        val dateParts = lastDate.split("/").map { it.trim().toIntOrNull() }
        if (dateParts.size != 3 || dateParts.any { it == null }) {
            throw IllegalArgumentException("Invalid date format: $lastDate")
        }

        val year = dateParts[0]!!
        val month = dateParts[1]!! - 1
        val day = dateParts[2]!!

        val calendar = Calendar.getInstance().apply {
            set(year, month, day)
            add(Calendar.DAY_OF_YEAR, intervalDays)
        }

        val formattedMonth = String.format("%02d", calendar.get(Calendar.MONTH) + 1)
        val formattedDay = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))
        return "${calendar.get(Calendar.YEAR)}/$formattedMonth/$formattedDay"
    }

}