package com.example.spap.Signup_login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.spap.R
import com.example.spap.databinding.FragmentSignupEmailBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignupEmail : Fragment() {
    private var _binding: FragmentSignupEmailBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

/*    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 추가적인 초기화가 필요하면 여기에 작성
    }*/

    fun getEmail(): String {
        return binding.emailEditText.text.toString()
    }

    suspend fun validateEmail(): Boolean {
        val email = getEmail()

        if (email.isEmpty()) {
            binding.emailEditText.error = "이메일을 입력해주세요!"
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEditText.error = "유효한 이메일 주소를 입력해주세요!"
            return false
        }

        return try {
            val isEmailAvailable = withContext(Dispatchers.IO) {
                // Firestore에서 이메일이 이미 존재하는지 확인
                val userDoc = firestore.collection("users").document(email).get().await()
                !userDoc.exists()
            }

            if (isEmailAvailable) {
                true // 이메일이 중복되지 않음
            } else {
                binding.emailEditText.error = "이미 사용 중인 이메일입니다!"
                false // 이메일이 이미 사용 중
            }
        } catch (e: Exception) {
            Log.e("SignupEmail", "Error checking email", e)
            Toast.makeText(context, "이메일 확인 중 오류가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}