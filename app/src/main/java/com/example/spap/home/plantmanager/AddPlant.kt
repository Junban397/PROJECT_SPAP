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
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import com.example.spap.R
import com.example.spap.databinding.ActivityAddPlantBinding
import com.google.android.material.snackbar.Snackbar
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
        val iYear = calendar.get(Calendar.YEAR)
        val iMonth = calendar.get(Calendar.MONTH)
        val iDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                val tMonth: Int = month + 1
                val date = "$year / $tMonth / $day"
                textView.setText(date)
            }, iYear, iMonth, iDay
        )
        datePicker.show()
    }

    private fun savePlantInfo() {
        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email

        val plantInfoMap = mutableMapOf<String, Any?>(
            "email" to email,
            "plantName" to binding.plantName.text.toString(),
            "plantType" to binding.plantType.text.toString(),
            "plantingDate" to binding.plantingDate.text.toString(),
            "plantLastWatering" to binding.lastWateredDate.text.toString(),
            "plantLastRepotted" to binding.lastRepottedDate.text.toString(),
            "plantWateringFrequency" to binding.plantWater.text.toString(),
            "plantRepottedFrequency" to binding.plantSoil.text.toString(),
            "plantHumidity" to binding.plantHumidity.text.toString(),
            "plantTemp" to binding.plantTemp.text.toString(),
            "plantMemo" to binding.plantMemo.text.toString()
        )

        if (imageUri != null) {
            uploadImageToStorage(imageUri!!) { imageUrl ->
                if (imageUrl != null) {
                    plantInfoMap["imageUrl"] = imageUrl
                }
                saveToFirestore(plantInfoMap)
            }
        } else {
            saveToFirestore(plantInfoMap)
        }
    }

    private fun saveToFirestore(plantInfoMap: Map<String, Any?>) {
        firestore.collection("plants").add(plantInfoMap)
            .addOnSuccessListener {
                val snackbar = Snackbar.make(binding.root, "식물 정보 저장 성공", Snackbar.LENGTH_SHORT)
                snackbar.show()

                // Schedule activity finish after 1 second
                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, 1000) // 1000 milliseconds = 1 second
            }
            .addOnFailureListener {
                Toast.makeText(this, "식물 정보 저장 실패", Toast.LENGTH_SHORT).show()
            }
    }
}