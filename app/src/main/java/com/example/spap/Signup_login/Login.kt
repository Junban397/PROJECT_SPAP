package com.example.spap.Signup_login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.spap.R
import com.example.spap.databinding.LoginActivityBinding

class Login : AppCompatActivity() {

    private lateinit var binding: LoginActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val Sign_Up_Btn=binding.singUpBtnLogin
        Sign_Up_Btn.setOnClickListener{
            val intent= Intent(this,Sign_up::class.java)
            startActivity(intent)
        }

    }
}