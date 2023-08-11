package com.example.frontend.restaurant

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.frontend.databinding.ActivityDetailBinding
import com.example.frontend.dto.Comment
import com.example.frontend.dto.CommentWithRating
import com.example.frontend.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var formattedTime: String
    private val comments = mutableListOf<CommentWithRating>()

    companion object {
        const val EXTRA_ITEM = "extra_item"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val item = intent.getStringExtra(EXTRA_ITEM)
        binding.detailTextView.text = "선택한 맛집: $item"

        binding.sendButton.setOnClickListener {
            val cmt = binding.commentInputEditText.text.toString()

            //별점 값
            val rating = binding.ratingBar.rating.toInt()

            if (rating == 0) {
                Toast.makeText(this@DetailActivity, "별점 필수!!", Toast.LENGTH_SHORT).show()
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
                    .baseUrl("http://10.100.103.15:8080") //백엔드 API 주소
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