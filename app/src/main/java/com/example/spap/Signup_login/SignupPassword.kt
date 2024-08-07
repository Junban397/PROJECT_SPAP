package com.example.spap.Signup_login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.spap.R
import com.example.spap.databinding.FragmentSignupNameBinding
import com.example.spap.databinding.FragmentSignupPasswordBinding

class SignupPassword : Fragment() {
    private var _binding: FragmentSignupPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun getPassword(): String {
        return binding.pwEditText.text.toString()
    }

    fun validatePassword(): Boolean {
        val password = getPassword()
        val rePassword = binding.confirmPwEditText.text.toString()

        val containsLetter = password.any { it.isLetter() }
        val containsDigit = password.any { it.isDigit() }

        return when {
            password.length < 8 -> {
                binding.pwEditText.error = "패스워드는 8글자 이상이여야 합니다!"
                false
            }

            !containsLetter || !containsDigit -> {
                binding.pwEditText.error = "비밀번호에는 영어와 숫자가 포함되어야 합니다!"
                false
            }

            password != rePassword -> {
                binding.confirmPwEditText.error = "비밀번호를 재확인 해주세요"
                false
            }

            else -> true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}