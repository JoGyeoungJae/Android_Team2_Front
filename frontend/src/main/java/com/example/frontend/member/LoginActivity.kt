package com.example.frontend.member

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.frontend.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.loginButton.setOnClickListener {
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()

            // 로그인 정보 확인 로직
            if (validateLogin(email, password)) {
                // 로그인 성공
                Toast.makeText(this,"로그인성공",Toast.LENGTH_SHORT).show()

            } else {
                // 로그인 실패
                Toast.makeText(this,"로그인실패",Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun validateLogin(email: String, password: String): Boolean {
        // TODO: MySQL 데이터베이스에서 입력한 email과 password를 확인하여 로그인 여부 판단
        // 여기서는 가상의 예시로 고정된 값을 사용합니다.
        val validEmail = "user@example.com"
        val validPassword = "password123"

        return email == validEmail && password == validPassword
    }

}