package com.example.spap.Signup_login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spap.Home.MainHome
import com.example.spap.R
import com.example.spap.databinding.LoginActivityBinding
import com.google.firebase.firestore.FirebaseFirestore

class Login : AppCompatActivity() {

    private lateinit var binding: LoginActivityBinding
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        val signUpBtn = binding.singUpBtnLogin
        signUpBtn.setOnClickListener {
            val intent = Intent(this, Sign_up::class.java)
            startActivity(intent)
        }

        val loginBtn = binding.loginBtn
        loginBtn.setOnClickListener {
            val email = binding.EmailLogin.text.toString()
            val password = binding.PassWdLogin.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "이메일과 비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        firestore.collection("users").document(email).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val storedPassword = document.getString("password")
                    if (storedPassword == password) {
                        // 로그인 성공
                        val intent = Intent(this, MainHome::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // 비밀번호 불일치
                        Toast.makeText(this, "비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // 이메일이 존재하지 않음
                    Toast.makeText(this, "등록되지 않은 이메일입니다.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "로그인에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
            }
    }
}