package com.example.spap.Signup_login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.spap.R
import com.example.spap.databinding.FragmentSignupEmailBinding
import com.example.spap.databinding.FragmentSignupNameBinding

class SignupName : Fragment() {
    private var _binding: FragmentSignupNameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun getName(): String {
        return binding.nameEditText.text.toString()
    }

    fun validateName(): Boolean {
        val name = getName()
        val containsInvalidChar = name.any {
            !it.isLetterOrDigit() && !it.isWhitespace()
        }
        return when{
            name.isEmpty() -> {
                binding.nameEditText.error="이름을 입력해주세요!"
                false
            }
            containsInvalidChar ->{
                binding.nameEditText.error="특수문자는 입력 불가입니다!"
                false
            }
            else -> true

        }


        return if (name.isEmpty()) {
            binding.nameEditText.error = "이름을 입력해주세요!"
            false
        } else {
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}