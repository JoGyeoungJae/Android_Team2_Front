package com.example.frontend.member

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.frontend.ApiService
import com.example.frontend.User
import com.example.frontend.databinding.ActivitySignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupButton.setOnClickListener {
            signUp()
        }
    }

    private fun signUp() {
        val uemail = binding.signupEmail.text.toString()
        val upassword = binding.signupPassword.text.toString()
        val uname = binding.signupName.text.toString()
        val unickname = binding.signupNickname.text.toString()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.100.103.14:8080/") // Spring Boot 서버의 URL로 변경
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        val user = User(uemail,upassword,uname,unickname)
//       val call = apiService.signup(id, password, email, phone)
        val call = apiService.signup(user)
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    // 성공적으로 응답을 받았을 때의 처리
                    Log.d("lsy","응답 왔어.")
                } else {
                    // 서버로부터 에러 응답을 받았을 때의 처리
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("NetworkError", "Error occurred: ${t.message}")
                // 네트워크 오류 등의 실패 처리
            }
        })
    }
}