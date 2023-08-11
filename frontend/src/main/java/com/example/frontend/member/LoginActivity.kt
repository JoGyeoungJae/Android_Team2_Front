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
//                var uid = ""
//                var uname = ""
//                var unickname = ""
//                var uimg = ""

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
                    val login = Login(uemail, upassword)
                    val apiService = retrofit.create(ApiService::class.java)

                    val call = apiService.login(login)
                    call.enqueue(object : Callback<ApiResponse<User>>{
                        override fun onResponse(call: Call<ApiResponse<User>>, response: Response<ApiResponse<User>>) {
                            if (response.isSuccessful) {
                                // 성공적으로 응답을 받았을 때의 처리
                                val responseBody = response.body()
                                val user = responseBody?.data
                                Log.d("lsy", "응답왔어. $user")

                            }else {
                                    val errorBody = response.errorBody()?.string()

                                    try{
                                    val errorJson = JSONObject(errorBody)
                                    val errorMessage = errorJson.getString("error")
                                        Log.d("lsy","응답x. 에러메시지 : $errorMessage")

                                    if(errorMessage.equals("No such email")){
                                        Toast.makeText(this@LoginActivity, "해당 이메일은 없습니다.", Toast.LENGTH_SHORT).show()
                                    }
                                    else if(errorMessage.equals("Incorrect password")){
                                        Toast.makeText(this@LoginActivity, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                                    } else{
                                        Toast.makeText(this@LoginActivity, "이메일또는 비밀번호가 다르거나 없습니다.", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: JSONException){

                                }
                            }
                        }

                        override fun onFailure(call: Call<ApiResponse<User>>, t: Throwable) {

                            Log.e("NetworkError", "Error occurred: ${t.message}")
                            // 네트워크 오류 등의 실패 처리
                        }
                    })
                }
            }

        }
}