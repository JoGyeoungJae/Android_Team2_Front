package com.example.frontend.restaurant

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.frontend.databinding.ActivityAddRestaurantBinding
import com.example.frontend.dto.FoodInfo
import com.example.frontend.service.FoodInfoService
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.util.UUID

class AddRestaurantActivity : AppCompatActivity() {
    //SHA1: 2F:60:49:C4:A4:71:5B:60:ED:7E:42:24:76:7E:DE:D4:5C:2E:E0:87
    lateinit var binding: ActivityAddRestaurantBinding
    //파일 경로를 전역으로 설정해서 갤러리에서 사진을 선택후 해당 파일의 절대 경로를 저장하는 파일
    lateinit var filePath: String
    private val storage = Firebase.storage
    lateinit var foodinfoService : FoodInfoService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRestaurantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.setimageBtn.setOnClickListener {
            /*Intent.ACTION_PICK -> 갤러리 사진 선택으로 이동*/
            val intent = Intent(Intent.ACTION_PICK)
            //인텐트 옵션에서 액션 문자열은, 이미지를 선택후, URI를 가져오는
            //데이터 타입, MIME TYPE, 모든 이미지
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*"
            )
            /*인텐트의 후처리를 호출하는 함수이고 위의 정의한 변수로 이동
            * */
            requestLauncher.launch(intent)
        }
        binding.addSave.setOnClickListener {
            if(binding.addImageView.drawable !== null){
                //store 에 먼저 데이터를 저장후 document id 값으로 업로드 파일 이름 지정
                Log.d("joj","@@@@@@@@@@@@@")

                val uuid = UUID.randomUUID()
                uploadImage(uuid.toString())
            }else {
                Toast.makeText(this, "데이터가 모두 입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }




        }

    }
    /*인텐트를 이용해서 후처리를 하는 코드ActivityResultContracts.StartActivityForResult())
    *
    * 사진 선택후 돌아왔을때 후처리하는 코드*/
    val requestLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult())
    {
        /*it 해당 정보를 담는 객체
        * 안드로이드 버전의 ok ,http status 200과 동일*/
        if(it.resultCode === android.app.Activity.RESULT_OK){
            //가져온 이미지를 처리를 글라이드를 이용
            Glide
                .with(getApplicationContext())
                //선택한 이미지를 불러오는 역할
                .load(it.data?.data)
                //출력 사진의 크기
                .apply(RequestOptions().override(250, 200))
                //사진의 크기를 조정해준다
                .centerCrop()
                //불러온 이미지를 결과뷰에 출력
                .into(binding.addImageView)

            /*커서 부분은 해당 이미지의 URI 경로로 위치를 파악하는 구문
            * 이미지의 위치가 있는 URI 주소,
            * MediaStore.Images.Media.DATA 이미지의 정보*/
            val cursor = contentResolver.query(it.data?.data as Uri,
                arrayOf<String>(MediaStore.Images.Media.DATA), null, null, null);
            cursor?.moveToFirst().let {
                // filePath=cursor?.getString(0) as String 경로 주소
                //log로 찍어서 확인 가능
                filePath=cursor?.getString(0) as String
            }
        }
    }
    private fun uploadImage(docId: String){
        //add............................
        val storageRef: StorageReference = storage.reference
        val imgRef: StorageReference = storageRef.child("profile_images/$docId.jpg")
        val bitmap = getBitmapFromView(binding.addImageView)
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = imgRef.putBytes(data)
        uploadTask.addOnSuccessListener {
            imgRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                val imageUrl = downloadUrl.toString()
                Toast.makeText(this, "save ok..", Toast.LENGTH_SHORT).show()
                Log.d("joj", "Image URL: $imageUrl")
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://10.100.103.71:8080/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val rtitle = binding.rtitle.text
                val rcity = binding.rcity.text
                val rlat = binding.rlat.text
                val rlng = binding.rlng.text
                val rtel = binding.rtel.text
                val rmainimg = imageUrl
                val rinfo = binding.rinfo.text
                Log.d("joj","저장되는 이미지경로${rmainimg}")

                foodinfoService = retrofit.create(FoodInfoService::class.java)
                val foodInfo = FoodInfo(null,rtitle.toString(), rcity.toString(), rlat.toString(), rlng.toString(),  rtel.toString(), rmainimg, rinfo.toString(),"0","0","0")
                val call = foodinfoService.postFoodInfo(foodInfo)
                call.enqueue(object : Callback<FoodInfo> {
                    override fun onResponse(call: Call<FoodInfo>, response: Response<FoodInfo>) {
                        if (response.isSuccessful) {
                            // 서버로부터 응답이 성공적으로 돌아온 경우 처리할 내용
                            // 예: Toast 메시지 표시 등
                        } else {
                            // 서버로부터 응답이 실패한 경우 처리할 내용
                            // 예: 에러 메시지 표시 등
                        }
                    }

                    override fun onFailure(call: Call<FoodInfo>, t: Throwable) {
                        // 통신에 실패한 경우 처리할 내용
                        // 예: 에러 메시지 표시 등
                    }
                })

                finish()
            }
        }
            .addOnFailureListener{
                Log.d("joj", "file save error", it)
            }

    }
    fun getBitmapFromView(view: View): Bitmap? {
        var bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bitmap)
        view.draw(canvas)
        return  bitmap
    }
}