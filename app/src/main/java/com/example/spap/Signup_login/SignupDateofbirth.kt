package com.example.spap.Signup_login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.spap.R
import com.example.spap.databinding.FragmentSignupDateofbirthBinding
import com.example.spap.databinding.FragmentSignupEmailBinding

class SignupDateofbirth : Fragment() {
    private var _binding: FragmentSignupDateofbirthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupDateofbirthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun getDateOfBirth(): String {
        return binding.dateOfBirthEditText.text.toString()
    }

    fun validateDateOfBirth(): Boolean {
        val dob = getDateOfBirth()
        return if (dob.isEmpty()) {
            binding.dateOfBirthEditText.error = "생년월일을 입력해주세요!"
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