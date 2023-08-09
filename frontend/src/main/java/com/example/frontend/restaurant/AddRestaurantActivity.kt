package com.example.frontend.restaurant

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.frontend.databinding.ActivityAddRestaurantBinding
import com.example.frontend.util.StorageApplication
import com.example.frontend.util.StorageApplication.Companion.storage
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddRestaurantActivity : AppCompatActivity() {
    //SHA1: 2F:60:49:C4:A4:71:5B:60:ED:7E:42:24:76:7E:DE:D4:5C:2E:E0:87
    lateinit var binding: ActivityAddRestaurantBinding
    //파일 경로를 전역으로 설정해서 갤러리에서 사진을 선택후 해당 파일의 절대 경로를 저장하는 파일
    lateinit var filePath: String
    private val storage = Firebase.storage
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
                uploadImage("testjojojo")
//                saveStore()
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

        val storageRef = storage.reference
        /*imgRef라는 개체로 업로드 및 다운로드 실행
        * child 상위 폴더 images 하위에 이미지 파일이 저장되는 구조*/
        val imgRef = storageRef.child("profile_images/${docId}.jpg")

        //후처리 코드에서 선택된 사진의 절대 경로  file 라고 하는 참조형 변수에 할당
        //val file = Uri.fromFile(File(filePath))
        val inputStream = contentResolver.openInputStream(Uri.parse(filePath))
        //imgRefㄹ의 기능중 putFile 경로의 파일을 업로드 하는 기능
        imgRef.putStream(inputStream!!)
            //이미지 업로드가 성공 하면 수행
            .addOnSuccessListener {
                Toast.makeText(this, "save ok..", Toast.LENGTH_SHORT).show()
                Log.d("joj","save ok..")
                //수동 종료
                finish()
            }
            .addOnFailureListener{
                Log.d("joj", "file save error", it)
            }

    }
}