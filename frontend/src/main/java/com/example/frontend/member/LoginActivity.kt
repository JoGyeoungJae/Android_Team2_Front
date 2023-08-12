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


                                    //로그인버튼 누르면 받아온 데이터를 쉐어드 값에 업로드하고 인텐트메인화면
                                    //
                                    //쉐어드의 값에따라 여러가지가 뜨도록 수정.
                                    //
                                    //로그아웃하면 쉐어드 값 초기화하고 인텐트 메인화면
                                    //
                                    //회원정보 수정은 버튼 누르면 db에 값 바꾸도록하고, 쉐어드도 수정하고 인텐트
                                    //
                                    //회원탈퇴는 db에있는 값 삭제하고, 쉐어드도 초기화하고 인텐트
                                    //
                                    //crud 개발완료했으니, 로그인상태(쉐어드 업로드)에서 하는 행위들마다 쉐어드데이터쓸수있게끔 ex)댓글, 사진



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