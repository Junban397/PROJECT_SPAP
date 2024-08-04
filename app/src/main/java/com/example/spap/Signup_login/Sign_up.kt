package com.example.spap.Signup_login

import android.os.Bundle
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.spap.R
import com.example.spap.databinding.SingUpActivityBinding

class Sign_up : AppCompatActivity() {
    private lateinit var binding: SingUpActivityBinding
    private lateinit var nextBtn: Button
    private lateinit var fragmentList: List<Fragment>
    private var currentFragmentIndex = 0

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
            showFragment(currentFragmentIndex)
        }

        nextBtn.setOnClickListener {
            if (nextBtn.text == "완료") {
                finish()
            } else {
                handleButtonClick()
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
            val newIndex = currentFragmentIndex + 1
            replaceFragment(fragmentList[newIndex], true)
            currentFragmentIndex = newIndex
            updateButtonText()
        }
    }

    private fun backButtonClick() {
        if (currentFragmentIndex > 0) {
            val newIndex = currentFragmentIndex - 1
            replaceFragment(fragmentList[newIndex], false)
            currentFragmentIndex = newIndex
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

    private fun showFragment(index: Int) {
        if (index in fragmentList.indices) {
            replaceFragment(fragmentList[index], true)
        }
    }

    private fun replaceFragment(fragment: Fragment, forward: Boolean) {
        val enterAnim = if (forward) R.anim.to_right else R.anim.to_left
        val exitAnim = if (forward) R.anim.from_right else R.anim.from_left

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(enterAnim, exitAnim)
            .replace(binding.fragmentSignup.id, fragment)
            .addToBackStack(null) // 백 스택에 추가
            .commit()
    }
}