package com.example.spap.Signup_login

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.spap.data.UserData
import com.example.spap.R
import com.example.spap.databinding.SingUpActivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Sign_up : AppCompatActivity() {

    private lateinit var binding: SingUpActivityBinding
    private lateinit var nextBtn: Button
    private lateinit var fragmentList: List<Fragment>
    private var currentFragmentIndex = 0
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private var userData = UserData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SingUpActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        nextBtn = binding.nextBtn

        fragmentList = listOf(
            SignupEmail(),
            SignupPassword(),
            SignupName(),
            SignupDateofbirth()
        )

        if (savedInstanceState == null) {
            showFragment(currentFragmentIndex, true)
        }

        nextBtn.setOnClickListener {
            lifecycleScope.launch {
                collectUserDataFrom()

                val isValid = validateCurrentDataFrom()

                Log.d("Sign_up", "Validation result for current fragment: $isValid")

                if (isValid) {
                    if (nextBtn.text == "완료") {
                        registerUser()
                    } else {
                        handleButtonClick()
                    }
                } else {
                    Log.d("Sign_up", "Validation failed for current fragment. No transition.")
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backButtonClick()
            }
        })
    }

    private suspend fun validateCurrentDataFrom(): Boolean {
        return when (currentFragmentIndex) {
            0 -> (fragmentList[currentFragmentIndex] as? SignupEmail)?.validateEmail() ?: false
            1 -> (fragmentList[currentFragmentIndex] as? SignupPassword)?.validatePassword()
                ?: false

            2 -> (fragmentList[currentFragmentIndex] as? SignupName)?.validateName() ?: false
            3 -> (fragmentList[currentFragmentIndex] as? SignupDateofbirth)?.validateDateOfBirth()
                ?: false

            else -> true
        }
    }

    private fun handleButtonClick() {
        if (currentFragmentIndex < fragmentList.size - 1) {
            currentFragmentIndex++
            showFragment(currentFragmentIndex, true)
            updateButtonText()
        }
    }

    private fun backButtonClick() {
        if (currentFragmentIndex > 0) {
            currentFragmentIndex--
            showFragment(currentFragmentIndex, false)
            updateButtonText()
        } else {
            finish()
        }
    }

    private fun updateButtonText() {
        nextBtn.text = if (currentFragmentIndex == fragmentList.size - 1) {
            "완료"
        } else {
            "계속하기"
        }
    }

    private fun showFragment(index: Int, forward: Boolean) {
        if (index in fragmentList.indices) {
            replaceFragment(fragmentList[index], forward)
        }
    }

    private fun replaceFragment(fragment: Fragment, forward: Boolean) {
        val enterAnim = if (forward) R.anim.to_right else R.anim.to_left
        val exitAnim = if (forward) R.anim.from_right else R.anim.from_left

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(enterAnim, exitAnim)
            .replace(binding.fragmentSignup.id, fragment)
            .commit()
    }

    private fun collectUserDataFrom() {
        when (currentFragmentIndex) {
            0 -> {
                val _userData = fragmentList[currentFragmentIndex] as? SignupEmail
                userData.email = _userData?.getEmail() ?: ""
            }

            1 -> {
                val _userData = fragmentList[currentFragmentIndex] as? SignupPassword
                userData.password = _userData?.getPassword() ?: ""
            }

            2 -> {
                val _userData = fragmentList[currentFragmentIndex] as? SignupName
                userData.name = _userData?.getName() ?: ""
            }

            3 -> {
                val _userData = fragmentList[currentFragmentIndex] as? SignupDateofbirth
                userData.dateOfBirth = _userData?.getDateOfBirth() ?: ""
            }
        }
    }

    private fun registerUser() {
        auth.createUserWithEmailAndPassword(userData.email, userData.password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        saveUserToStore()
                    }
                } else {
                    Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()

                }
            }
    }

    private fun saveUserToStore() {
        val userMap = mapOf(
            "email" to userData.email,
            "name" to userData.name,
            "dateOfBirth" to userData.dateOfBirth
        )

        firestore.collection("users").document(userData.email)
            .set(userMap)
            .addOnSuccessListener {
                Log.d("Sign_up", "사용자 정보 저장 성공")
                finish()
            }
            .addOnFailureListener { e ->
                Log.e("Sign_up", "사용자 정보 저장 실패", e)
            }
    }
}
