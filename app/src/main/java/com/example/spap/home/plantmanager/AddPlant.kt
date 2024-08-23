package com.example.spap.home.plantmanager

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import com.example.spap.R
import com.example.spap.databinding.ActivityAddPlantBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.Calendar
import java.util.UUID

class AddPlant : AppCompatActivity() {
    private var _binding: ActivityAddPlantBinding? = null
    private val binding get() = _binding!!
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var waterCycleDate: Int = 0
    private var soilCycleDate: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        _binding = ActivityAddPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            addPlantImg.setOnClickListener { openGallery() }
            plantingDate.setOnClickListener { setDate(plantingDate) }
            lastWateredDate.setOnClickListener { setDate(lastWateredDate) }
            lastRepottedDate.setOnClickListener { setDate(lastRepottedDate) }
            addPlantBtn.setOnClickListener { savePlantInfo() }
            plantWater.setOnClickListener { setPlantCycleInfo(plantWater) }
            plantSoil.setOnClickListener { setPlantCycleInfo(plantSoil) }
            plantTemp.setOnClickListener { setPlantInfo(plantTemp, "℃") }
            plantHumidity.setOnClickListener { setPlantInfo(plantHumidity, "%") }
        }
    }

    private fun setPlantCycleInfo(setPlantInfo: TextView) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_cycle)

        val confirmBtn = dialog.findViewById<Button>(R.id.confirm_cycle_btn)
        val cancelBtn = dialog.findViewById<Button>(R.id.cancel_cycle_btn)
        val dayNumPicker = dialog.findViewById<NumberPicker>(R.id.dayNum)
        val cyclePicker = dialog.findViewById<NumberPicker>(R.id.cycle)
        val cycleOption = arrayOf("일", "주", "달")

        dayNumPicker.minValue = 1
        dayNumPicker.maxValue = 31
        dayNumPicker.wrapSelectorWheel = false

        cyclePicker.minValue = 0
        cyclePicker.maxValue = 2
        cyclePicker.displayedValues = cycleOption

        confirmBtn.setOnClickListener {
            val selectedDay = dayNumPicker.value
            val selectedCycle = cycleOption[cyclePicker.value]
            val days = when (selectedCycle) {
                "일" -> selectedDay
                "주" -> selectedDay * 7
                "달" -> selectedDay * 30
                else -> selectedDay
            }
            if (setPlantInfo == binding.plantWater) {
                waterCycleDate = days
            } else if (setPlantInfo == binding.plantSoil) {
                soilCycleDate = days
            }
            setPlantInfo.text = "$selectedDay$selectedCycle 마다"
            dialog.dismiss()
        }
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setPlantInfo(setPlantInfo: TextView, type: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_plantinfo)

        val confirmBtn = dialog.findViewById<Button>(R.id.confirm_btn)
        val cancelBtn = dialog.findViewById<Button>(R.id.cancel_btn)
        val minInfo = dialog.findViewById<EditText>(R.id.min_info)
        val maxInfo = dialog.findViewById<EditText>(R.id.max_info)

        confirmBtn.setOnClickListener {
            val minInfo_ = minInfo.text.toString()
            val maxInfo_ = maxInfo.text.toString()
            setPlantInfo.text = "$minInfo_ ~ $maxInfo_ $type"
            dialog.dismiss()
        }
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            binding.addPlantImg.setImageURI(imageUri)
        }
    }

    private fun uploadImageToStorage(uri: Uri, onComplete: (String?) -> Unit) {
        val storageRef = storage.reference.child("plant_images/${UUID.randomUUID()}.jpg")
        val uploadTask = storageRef.putFile(uri)

        uploadTask.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                onComplete(downloadUri.toString())
            }.addOnFailureListener {
                onComplete(null)
            }
        }.addOnFailureListener { exception ->
            onComplete(null)
        }
    }

    private fun setDate(textView: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                val formattedMonth = String.format("%02d", selectedMonth + 1)  // 월을 2자리로 포맷
                val formattedDay = String.format("%02d", selectedDay)  // 일을 2자리로 포맷
                val date = "$selectedYear/$formattedMonth/$formattedDay"  // yyyy/MM/dd 형식으로 저장
                textView.setText(date)
            }, year, month, day
        )
        datePicker.show()
    }

    private fun savePlantInfo() {
        // 입력 필드 값 가져오기
        val plantName = binding.plantName.text.toString().trim()
        val plantType = binding.plantType.text.toString().trim()
        val plantingDate = binding.plantingDate.text.toString().trim()
        val plantWateringFrequency = binding.plantWater.text.toString().trim()
        val plantRepottedFrequency = binding.plantSoil.text.toString().trim()
        val plantHumidity = binding.plantHumidity.text.toString().trim()
        val plantTemp = binding.plantTemp.text.toString().trim()
        val plantMemo = binding.plantMemo.text.toString().trim()


        // 빈 필드가 있는지 확인
        if (plantName.isEmpty() || plantType.isEmpty() || plantingDate.isEmpty() ||
            plantWateringFrequency.isEmpty() || plantRepottedFrequency.isEmpty() ||
            plantHumidity.isEmpty() || plantTemp.isEmpty()) {
            Toast.makeText(this, "모든 필드를 채워주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        // Firebase 저장 로직
        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email ?: return

        val plantInfoMap = mutableMapOf<String, Any?>(
            "email" to email,
            "plantName" to plantName,
            "plantType" to plantType,
            "plantingDate" to plantingDate,
            "plantWateringFrequency" to plantWateringFrequency,
            "plantRepottedFrequency" to plantRepottedFrequency,
            "plantHumidity" to plantHumidity,
            "plantTemp" to plantTemp,
            "plantMemo" to plantMemo
        )

        if (imageUri != null) {
            uploadImageToStorage(imageUri!!) { imageUrl ->
                if (imageUrl != null) {
                    plantInfoMap["imageUrl"] = imageUrl
                }
                saveToFirestore(plantInfoMap) { documentId ->
                    if (documentId != null) {
                        saveSchedules(email, documentId)
                        showToastAndFinish("식물 정보가 저장되었습니다.")
                    }
                }
            }
        } else {
            saveToFirestore(plantInfoMap) { documentId ->
                if (documentId != null) {
                    saveSchedules(email, documentId)
                    showToastAndFinish("식물 정보가 저장되었습니다.")
                }
            }
        }
    }

    private fun saveToFirestore(plantInfoMap: Map<String, Any?>, onComplete: (String?) -> Unit) {
        firestore.collection("plants").add(plantInfoMap)
            .addOnSuccessListener { documentReference ->
                onComplete(documentReference.id)
            }
            .addOnFailureListener {
                Toast.makeText(this, "식물 정보 저장 실패", Toast.LENGTH_SHORT).show()
                onComplete(null)
            }
    }

    private fun saveSchedules(email: String, pId: String) {
        val schedules = listOf(
            "watering" to mapOf(
                "email" to email,
                "plantId" to pId,  // 식물 식별자 추가
                "type" to "물주기",
                "lastDate" to binding.lastWateredDate.text.toString(),
                "intervalDays" to waterCycleDate,
                "nextDate" to nextDate(binding.lastWateredDate.text.toString(), waterCycleDate)
            ),
            "repotting" to mapOf(
                "email" to email,
                "plantId" to pId,  // 식물 식별자 추가
                "type" to "분갈이",
                "lastDate" to binding.lastRepottedDate.text.toString(),
                "intervalDays" to soilCycleDate,
                "nextDate" to nextDate(binding.lastRepottedDate.text.toString(), soilCycleDate)
            )
        )

        schedules.forEach { (type, schedule) ->
            firestore.collection("schedules").document("${type}_$pId").set(schedule)
        }
    }

    private fun nextDate(lastDate: String, intervalDays: Int): String {
        val dateParts = lastDate.split("/").map { it.trim() }

        if (dateParts.size != 3) {
            throw IllegalArgumentException("Invalid date format: $lastDate")
        }

        val year = dateParts[0].toIntOrNull() ?: throw IllegalArgumentException("Invalid year in date: $lastDate")
        val month = dateParts[1].toIntOrNull()?.minus(1) ?: throw IllegalArgumentException("Invalid month in date: $lastDate")
        val day = dateParts[2].toIntOrNull() ?: throw IllegalArgumentException("Invalid day in date: $lastDate")

        val calendar = Calendar.getInstance().apply {
            set(year, month, day)
            add(Calendar.DAY_OF_YEAR, intervalDays)
        }

        val formattedMonth = String.format("%02d", calendar.get(Calendar.MONTH) + 1)
        val formattedDay = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))
        return "${calendar.get(Calendar.YEAR)}/$formattedMonth/$formattedDay"
    }
    private fun showToastAndFinish(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, 2000)  // Toast가 보일 시간을 충분히 주기 위해 2초 지연
    }
}