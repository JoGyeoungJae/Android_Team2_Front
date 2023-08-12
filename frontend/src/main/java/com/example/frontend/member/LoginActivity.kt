package com.example.frontend.member

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.frontend.databinding.ActivityLoginBinding
import com.example.frontend.dto.ApiResponse
import com.example.frontend.dto.Login
import com.example.frontend.dto.User
import com.example.frontend.service.ApiService
import com.google.gson.GsonBuilder
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


            binding.loginButton.setOnClickListener {
                val uemail = binding.loginEmail.text.toString()
                val upassword = binding.loginPassword.text.toString()
                var uid = ""
                var uname = ""
                var unickname = ""
                var uimg = ""

                if (uemail.isEmpty() || upassword.isEmpty()) {
                    // 어떤 입력값이 비어있으면 토스트 메시지 표시
                    Toast.makeText(this, "모든 값을 입력하세요.", Toast.LENGTH_SHORT).show()
                } else {

                    //서버로 값 전송
                    val retrofit = Retrofit.Builder()
                        .baseUrl("http://10.100.103.16:8080/") // Spring Boot 서버의 URL로 변경
                        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                        .build()





//                    val login = Login(uemail, upassword, uid, uname, unickname, uimg)
                    val login = Login(uemail, upassword, uid, uname, unickname, uimg)
                    val apiService = retrofit.create(ApiService::class.java)

                    val call = apiService.login(login)
                    call.enqueue(object : Callback<ApiResponse<Login>> {
                        override fun onResponse(call: Call<ApiResponse<Login>>, response: Response<ApiResponse<Login>>) {
                            val apiResponse = response.body()

                            if (apiResponse != null) {
                                if (apiResponse.success) {
                                    val user = apiResponse.data
                                    Log.d("lys","응답 o $user")
                                    // 로그인 성공 처리 (예: 화면 전환 등)
                                    Toast.makeText(this@LoginActivity, "로그인 성공!", Toast.LENGTH_SHORT).show()
                                } else {
                                    val errorMessage = apiResponse.error
                                    Log.d("lys","응답 x $errorMessage")
                                    if (errorMessage == "No such email") {
                                        Toast.makeText(this@LoginActivity, "해당 이메일은 없습니다.", Toast.LENGTH_SHORT).show()
                                    } else if (errorMessage == "Incorrect password") {
                                        Toast.makeText(this@LoginActivity, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(this@LoginActivity, "이메일또는 비밀번호가 다르거나 없습니다.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                // 응답이 null인 경우에 대한 처리
                                Log.d("lys","응답 null")
                            }
                        }

                        override fun onFailure(call: Call<ApiResponse<Login>>, t: Throwable) {
                            // 네트워크 요청 실패에 대한 처리
                            Log.d("lys","네트워크요청실패")
                        }
                    })

                }
            }

        }
}