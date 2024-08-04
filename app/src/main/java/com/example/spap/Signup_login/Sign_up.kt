package com.example.spap.Signup_login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.spap.databinding.SingUpActivityBinding

class Sign_up : AppCompatActivity() {

    private lateinit var binding: SingUpActivityBinding
    private lateinit var nextBtn:Button
    private lateinit var fragmentList: List<Fragment>
    private var currentFragmentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SingUpActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nextBtn=binding.nextBtn

        fragmentList= listOf(
            SignupEmail(),
            SignupPassword(),
            SignupName(),
            SignupDateofbirth()
        )
        // 초기 프래그먼트 설정
        showFragment(currentFragmentIndex)

        nextBtn.setOnClickListener {
            handleButtonClick()
        }
    }

    private fun handleButtonClick() {
        // 다음 프래그먼트로 전환
        currentFragmentIndex++
        if (currentFragmentIndex < fragmentList.size-1) {
            showFragment(currentFragmentIndex)
        } else {
            // 모든 프래그먼트를 다 본 후
            showFragment(currentFragmentIndex)
            nextBtn.text = "완료"
            nextBtn.setOnClickListener {
                finish() // 액티비티 종료
            }
        }
    }

    private fun showFragment(index: Int) {
        val fragment = fragmentList[index]
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentSignup.id, fragment) // 뷰 바인딩을 통해 ID를 가져옵니다.
            .commit()
    }
}