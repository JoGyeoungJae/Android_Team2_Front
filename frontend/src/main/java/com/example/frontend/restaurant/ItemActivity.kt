package com.example.frontend.restaurant

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatRatingBar
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
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ItemActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    lateinit var toggle: ActionBarDrawerToggle  // 메뉴
    lateinit var binding: ActivityItemBinding
    private lateinit var formattedTime: String
    private val comments = mutableListOf<CommentWithRating>()
    private val storage = Firebase.storage
    private lateinit var filePath: String

    //11
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityItemBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //갤러리 요청 런처mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
        val requestGalleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        {
            try {
                //calRatio는 원본의 사진을 얼마나 줄일지 비율 값을 나타냄
                val calRatio = calculateInSampleSize(
                    it.data!!.data!!,
                    resources.getDimensionPixelSize(R.dimen.imgSize),
                    resources.getDimensionPixelSize(R.dimen.imgSize)
                )
                val option = BitmapFactory.Options()
                option.inSampleSize = calRatio

                //이미지 로딩
                //사진을 바이트 단위로 읽었음. inputStream : 이미지의 바이트 단위의 결과값
                var inputStream = contentResolver.openInputStream(it.data!!.data!!)!!
                val bitmap = BitmapFactory.decodeStream(inputStream, null, option)
                inputStream!!.close()
                bitmap?.let {
                    binding.reviewImageView.setImageBitmap(bitmap)
                } ?: let{
                    Log.d("kkang", "bitmap null")
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
        //mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm


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
        //============================사진 불러오기 버튼=========================
        binding.setimageBtn.setOnClickListener {


            //갤러리 앱
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            requestGalleryLauncher.launch(intent)
        }

        //카메라 요청 런처
        val requestCameraFileLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        {
            //카메라 앱
            val calRatio = calculateInSampleSize(
                Uri.fromFile(File(filePath)),
                resources.getDimensionPixelSize(R.dimen.imgSize),
                resources.getDimensionPixelSize(R.dimen.imgSize)
            )
            val option = BitmapFactory.Options()
            option.inSampleSize = calRatio
            val bitmap = BitmapFactory.decodeFile(filePath, option)
            bitmap?.let {
                binding.reviewImageView.setImageBitmap(bitmap)
            }


        }
        //==========================리뷰=====================
        binding.sendButton.setOnClickListener {

            //갤러리에서 가져온 이미지를 파이어베이스에 저장하기 전에 선언해야할부분mmmmmmmmmmmmmmmmmmmmmmmmmm
            val storageRef: StorageReference = storage.reference
            //파이어베이스 스토리지의 profile_images 라는 패키지 안에  reviewimg.jpg 라는 이미지로 바꿔야된다.
            val imgRef: StorageReference = storageRef.child("profile_images/reviewimg.jpg")

            val bitmap = getBitmapFromView(binding.reviewImageView)
            val baos = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            //mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm

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
                val uid="adimin"
                comments.add(CommentWithRating(cmt, rating, currentTime, uid))

                if (data.isEmpty()) {
                    //이미지가 없는 경우
                    val noImageMessage = "이미지 없음"
                    comments.add(CommentWithRating(cmt, rating, currentTime, uid, noImageMessage))
                    binding.reviewImageView.setImageResource(R.drawable.no_image_placeholder)
                } else {
                    //이미지가 있는 경우
                    //파이어베이스에 사진을 올리고, 그 사진의 url을 따온다음? 다른데이터와 함께 스프링부트에 전송mmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
                    var uploadTask = imgRef.putBytes(data)
                    uploadTask.addOnSuccessListener { _ ->
                        Log.d("lsy", "이미지 업로드 성공")
                        // 이미지 업로드 후 다운로드 URL 가져오기
                        imgRef.downloadUrl.addOnSuccessListener { uri ->
                            val downloadUrl = uri.toString()
                            Log.d("lsy", "Download URL: $downloadUrl")
                            val reviewimg = downloadUrl

                            // TODO: 필요한 대로 downloadUrl을 사용합니다.
                            //Retrofit 인스턴스 생성, 서버로 값 전송
                            val retrofit = Retrofit.Builder()
                                .baseUrl("http://10.100.103.23:8080/") //백엔드 API 주소
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()
                            val uid="adimin"
                            val comment = Comment(cmt, formattedTime, reviewimg, uid) //Comment 클래스는 댓글 데이터 모델을 나타냄
                            val apiService = retrofit.create(ApiService::class.java)

                            //댓글, 시간, 등록한 이미지의 url이 comment에 담겨있는데,
                            //ApiService 안에 선언한 함수 @POST("comments") postComment 이것을 통해서 스프링부트에다가 POST하는것
                            val call = apiService.postComment(comment)

                            //이 부분은 POST한 이후 응답을 처리하는 부분
                            call.enqueue(object : Callback<Comment> {
                                override fun onResponse(
                                    call: Call<Comment>,
                                    response: Response<Comment>
                                ) {
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

                //댓글 작성 후 달린 댓글 및 평균 별점 표시
                updateCommentTextViewAndRating()

                //댓글 입력 필드 및 별점 초기화
                binding.commentInputEditText.text.clear()
                binding.ratingBar.rating = 0.0f


                //댓글 등록 후 키패드 닫기
                hideKeyboard()

                //mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm

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

        var combinedComments = ""
        for(comment in comments) {
            if(comment.message == "이미지 없음")  {
                combinedComments += "별점 : ${comment.rating} \n 이미지 없음 ${comment.comment} ${formattedTime}\n"
            } else {
                combinedComments += "별점 : ${comment.rating} \n ${comment.comment} ${formattedTime}\n"
            }
        }

//        combinedComments = comments.joinToString("\n") {
//            "${it.comment} (별점: ${it.rating}, 시간: $formattedTime)"
//        }
        binding.commentTextView.text = "\n$combinedComments"

//        val averageRating = calculateAverageRating()
//        binding.ratingAverageTextView.text = "평균 별점: %.2f점".format(averageRating)
    }

    private fun calculateAverageRating() : Double {
        if (comments.isEmpty()) {
            return 0.0
        }

        val totalRating = comments.sumBy { it.rating }
        return totalRating.toDouble() / comments.size
    }



    //사진사이즈 조절하는 함수
    private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
        //비트맵 객체 그대로 사용하면, 사진 원본을 그대로 사용해서 메모리 부족 현상 생김.
        // 그래서, 옵션이라는 속성을 사용.
        val options = BitmapFactory.Options()
        // 실제 비트맵 객체를 생성하는 것 아니고, 옵션 만 설정하겠다라는 의미.
        options.inJustDecodeBounds = true
        try {
            // 실제 원본 사진의 물리 경로에 접근해서, 바이트로 읽음.
            // 사진을 읽은 바이트 단위.
            var inputStream = contentResolver.openInputStream(fileUri)

            //inJustDecodeBounds 값을 true 로 설정한 상태에서 decodeXXX() 를 호출.
            //로딩 하고자 하는 이미지의 각종 정보가 options 에 설정 된다.
            BitmapFactory.decodeStream(inputStream, null, options)
            // 읽었던 원본의 사진의 메모리 사용은 반납.
            inputStream!!.close()
            // 객체를 null 초기화,//
            inputStream = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //비율 계산........................
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1
        //inSampleSize 비율 계산
        //height ,width 원본의 가로 세로 크기.
        // reqHeight, reqWidth 원하는 크기 사이즈,
        // 이것보다 크면 원본의 사이즈를 반으로 줄이는 작업을 계속 진행.
        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    fun getBitmapFromView(view: View): Bitmap? {
        var bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bitmap)
        view.draw(canvas)
        return  bitmap
    }
}