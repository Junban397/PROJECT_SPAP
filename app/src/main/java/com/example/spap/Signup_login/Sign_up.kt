package com.example.spap.Signup_login

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.spap.Entity.UserData
import com.example.spap.R
import com.example.spap.databinding.SingUpActivityBinding

class Sign_up : AppCompatActivity() {
    private lateinit var binding: SingUpActivityBinding
    private lateinit var nextBtn: Button
    private lateinit var fragmentList: List<Fragment>
    private var currentFragmentIndex = 0

    private var userData = UserData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SingUpActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nextBtn = binding.nextBtn

        fragmentList = listOf(
            SignupEmail(),
            SignupPassword(),
            SignupName(),
            SignupDateofbirth()
        )

        // 초기 프래그먼트 설정
        if (savedInstanceState == null) {
            showFragment(currentFragmentIndex, true)
        }

        nextBtn.setOnClickListener {
            if (nextBtn.text == "완료") {
                if (validateCurrentDataFrom()) {
                    collectUserDataFrom()
                    finish()
                }
            } else {
                if (validateCurrentDataFrom()) {
                    collectUserDataFrom()
                    handleButtonClick()
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backButtonClick()
            }
        })
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

    ////////////////////////////////

    private fun collectUserDataFrom() {
        when (currentFragmentIndex) {
            0 -> {
                val _userData = fragmentList[currentFragmentIndex] as SignupEmail
                userData.email = _userData.getEmail()
            }

            1 -> {
                val _userData = fragmentList[currentFragmentIndex] as SignupPassword
                userData.password = _userData.getPassword()
            }

            2 -> {
                val _userData = fragmentList[currentFragmentIndex] as SignupName
                userData.name = _userData.getName()
            }

            3 -> {
                val _userData = fragmentList[currentFragmentIndex] as SignupDateofbirth
                userData.dateOfBirth = _userData.getDateOfBirth()
            }
        }
        Log.d(
            "Sign_up",
            "Date of Birth: ${userData.email + userData.password + userData.name + userData.dateOfBirth}"
        )
    }

    private fun validateCurrentDataFrom(): Boolean {
        return when (currentFragmentIndex) {
            0 -> {
                val _validate = fragmentList[currentFragmentIndex] as SignupEmail
                _validate.validateEmail()
            }

            1 -> {
                val _validate = fragmentList[currentFragmentIndex] as SignupPassword
                _validate.validatePassword()
            }

            2 -> {
                val _validate = fragmentList[currentFragmentIndex] as SignupName
                _validate.validateName()
            }

            3 -> {
                val _validate = fragmentList[currentFragmentIndex] as SignupDateofbirth
                _validate.validateDateOfBirth()
            }

            else -> true
        }
    }
}