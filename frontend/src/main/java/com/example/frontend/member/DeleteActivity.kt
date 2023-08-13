package com.example.frontend.member

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.frontend.R
import com.example.frontend.databinding.ActivityDeleteBinding
import com.example.frontend.db.DBConnect2
import com.example.frontend.dto.User
import com.example.frontend.main.MainActivity
import com.example.frontend.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeleteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // SharedPreferences 객체생성=================저장된 값을 가져오기 위해=====================================
        val sharedPreferences = getSharedPreferences("logged_user", Context.MODE_PRIVATE)
        val uid = sharedPreferences.getString("uid", null)
        val uemail = sharedPreferences.getString("uemail", null)
        val upassword = sharedPreferences.getString("upassword", null)
        val uname = sharedPreferences.getString("uname", null)
        val unickname = sharedPreferences.getString("unickname", null)
        val uimg = sharedPreferences.getString("uimg", null)


        Log.d("lys","uid : $uid")
        Log.d("lys","uemail : $uemail")
        Log.d("lys","upassword : $upassword")
        Log.d("lys","uname : $uname")
        Log.d("lys","unickname : $unickname")
        Log.d("lys","uimg : $uimg")


        val deleteEmail = findViewById<TextView>(R.id.deleteEmail)
        deleteEmail.text = "$uemail"
        val deleteName = findViewById<TextView>(R.id.deleteName)
        deleteName.text = "$uname"
        val deleteNickname = findViewById<TextView>(R.id.deleteNickname)
        deleteNickname.text = "$unickname"


        binding.deleteButton.setOnClickListener {
            delete(uid, uemail, upassword, uname, unickname, uimg)

        }

    }

    private fun delete(uid:String?, uemail:String?, upassword:String?, uname:String?, unickname:String?, uimg:String?) {

        val demail = uemail.toString()
        val dpassword = upassword.toString()
        val dname = uname.toString()
        val dnickname = unickname.toString()
        val dimg = uimg.toString()

        val retrofit = DBConnect2.retrofit
        val user = User(demail, dpassword, dname, dnickname, dimg)
        val apiService = retrofit.create(ApiService::class.java)

        val call = apiService.delete(user)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    // 성공적으로 응답을 받았을 때의 처리
                    Log.d("lys", "갤러리부분")
                    val check = response.body()
                    // 성공적으로 응답을 받았을 때의 처리. 입력한 값을 db에 저장하고 나서 ok! 를 리턴함
                    Log.d("lys", "응답 o, $check")

                    //ok! 가 리턴되었다는건 정상적으로 저장이 되었다는 뜻이니까
                    if(check.equals("ok!")){

                        //탈퇴했으니 쉐어드프리퍼런스 초기화
                        val sharedPreferences = getSharedPreferences("logged_user", Context.MODE_PRIVATE)
                        var logged = sharedPreferences.edit()
                        logged.putString("uid", null)
                        logged.putString("uemail", null)
                        logged.putString("upassword", null)
                        logged.putString("uname", null)
                        logged.putString("unickname", null)
                        logged.putString("uimg", null)
                        logged.apply()

                        //정상적으로 동작하면 다른 화면으로 이동하게끔
                        Toast.makeText(this@DeleteActivity,"회원탈퇴 완료", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@DeleteActivity, MainActivity::class.java)
                        startActivity(intent)
                    }

                } else {
                    // 서버로부터 에러 응답을 받았을 때 처리
                    Log.d("lys", "응답x")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("lys", "Error occurred: ${t.message}")
                // 네트워크 오류 등의 실패 처리
            }
        })

    }
}