package com.example.frontend.restaurant

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.frontend.R
import com.example.frontend.databinding.ActivityItemBinding
import com.example.frontend.dto.Comment
import com.example.frontend.dto.CommentWithRating
import com.example.frontend.member.LoginActivity
import com.example.frontend.member.SignupActivity
import com.example.frontend.service.ApiService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.internal.ViewUtils.hideKeyboard
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ItemActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    lateinit var toggle: ActionBarDrawerToggle  // 메뉴
    lateinit var binding: ActivityItemBinding
    private lateinit var formattedTime: String
    private val comments = mutableListOf<CommentWithRating>()


    //1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //====================토글 메뉴==========================
        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawer,
            R.string.drawer_opened,
            R.string.drawer_closed
        )
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        binding.mainDrawerView.setNavigationItemSelectedListener {
            if(it.itemId == R.id.joinmenu){
                val intent = Intent(this, SignupActivity::class.java)
                startActivity(intent)
            }else if(it.itemId == R.id.login){
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            true
        }
        //====================토글 메뉴==========================
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        // 값을 받기 위해 Intent 가져오기
        val intent = intent

        // 전달된 값 가져오기
        val rid = intent.getLongExtra("rid",0L)
        val rtitle = intent.getStringExtra("rtitle")
        val rcity = intent.getStringExtra("rcity")
        val rtel = intent.getStringExtra("rtel")
        val rinfo = intent.getStringExtra("rinfo")

        binding.rid.text = rid.toString()
        binding.rtitle.text = rtitle
        binding.rcity.text = rcity
        binding.rtel.text = rtel
        binding.rinfo.text = rinfo
        val img = intent.getStringExtra("rmainimg")
        Log.d("joj",img.toString())
        Glide.with(this)
            .asBitmap()
            .load(img)
            .into(object : CustomTarget<Bitmap>(200, 200) {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    binding.imgpath.setImageBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    TODO("Not yet implemented")
                }
            })

        //==========================리뷰=====================
        binding.sendButton.setOnClickListener {
            val cmt = binding.commentInputEditText.text.toString()

            //별점 값
            val rating = binding.ratingBar.rating.toInt()

            if (rating == 0) {
                Toast.makeText(this@ItemActivity, "별점 필수!!", Toast.LENGTH_SHORT).show()
                //별점을 매기지 않은 경우 처리
                // (예: 에러 메시지 표시 또는 별점 필수 입력)
            } else {
                //댓글과 별점을 결합하여 저장
                val currentTime = System.currentTimeMillis()
                val dataFormat = SimpleDateFormat("(yyyy-MM-dd, HH:mm)", Locale.getDefault())
                formattedTime = dataFormat.format(Date(currentTime))

                comments.add(CommentWithRating(cmt, rating, currentTime))

                //댓글 작성 후 달린 댓글 및 평균 별점 표시
                updateCommentTextViewAndRating()

                //댓글 입력 필드 및 별점 초기화
                binding.commentInputEditText.text.clear()
                binding.ratingBar.rating = 0.0f

                //댓글 등록 후 키패드 닫기
                hideKeyboard()


//                //현재 시간을 가져와서 포맷팅
//                val dataFormat = SimpleDateFormat("(yyyy-MM-dd HH:mm)", Locale.getDefault())
//                val formattedTime = dataFormat.format(Date(currentTime))

                //Retrofit 인스턴스 생성
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://10.100.103.71:8080/") //백엔드 API 주소
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val comment = Comment(cmt, formattedTime) //Comment 클래스는 댓글 데이터 모델을 나타냄
                val apiService = retrofit.create(ApiService::class.java)

                //댓글 등록 요청 보내기
                val call = apiService.postComment(comment)
                call.enqueue(object : Callback<Comment> {
                    override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                        //요청이 성공적으로 처리되었을 때 실행되는 코드
                        //댓글 등록 후, 등록한 시간을 commentTimeTextView에 업데이트
                        binding.commentTimeTextView.text = formattedTime

                        //댓글 입력 필드 및 별점 초기화
                        binding.commentInputEditText.text.clear()
                        binding.ratingBar.rating = 0.0f
                    }

                    override fun onFailure(call: Call<Comment>, t: Throwable) {
                        //요청이 실패했을 때 실행되는 코드
                    }
                })




            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        val intent = intent
        val LAT = intent.getDoubleExtra("rlat",0.0) // String을 Double로 변환
        val LNG = intent.getDoubleExtra("rlng",0.0)

        if (LAT != null && LNG != null) {
            Log.d("joj", LAT.toString())
            Log.d("joj", LNG.toString())
            mMap = googleMap

            // Add a marker at the specified location and move the camera
            val location = LatLng(LAT, LNG)
            mMap.addMarker(MarkerOptions().position(location).title("Marker"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 20f))
        } else {
            Log.d("joj", "LAT or LNG is null.")
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.commentInputEditText.windowToken, 0)
    }

    private fun updateCommentTextViewAndRating() {
        val combinedComments = comments.joinToString("\n") {
            "${it.comment} (별점: ${it.rating}, 시간: $formattedTime)"
        }
        binding.commentTextView.text = "\n$combinedComments"

        val averageRating = calculateAverageRating()
        binding.ratingAverageTextView.text = "평균 별점: %.2f점".format(averageRating)
    }

    private fun calculateAverageRating() : Double {
        if (comments.isEmpty()) {
            return 0.0
        }

        val totalRating = comments.sumBy { it.rating }
        return totalRating.toDouble() / comments.size
    }
}