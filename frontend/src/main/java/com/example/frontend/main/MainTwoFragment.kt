package com.example.frontend.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontend.databinding.FragmentMainTwoBinding
import com.example.frontend.dto.FoodInfo
import com.example.frontend.recycler.MyAdapter2
import com.example.frontend.service.FoodInfoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainTwoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainTwoFragment : Fragment() {
    lateinit var binding: FragmentMainTwoBinding
    lateinit var foodinfoService : FoodInfoService
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainTwoBinding.inflate(inflater, container, false)
        // binding 초기화
        // Inflate the layout for this fragment
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.100.103.71:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        foodinfoService = retrofit.create(FoodInfoService::class.java)

        val call: Call<List<FoodInfo?>?>? = foodinfoService.getFoodInfoList()
        Log.d("joj",call.toString())
        call?.enqueue(object : Callback<List<FoodInfo?>?> {
            override fun onResponse(call: Call<List<FoodInfo?>?>, response: Response<List<FoodInfo?>?>) {
                if (response.isSuccessful) {
                    val foods = response.body()
                    // 여기서 받아온 데이터(items)를 처리합니다.
                    val adapter = MyAdapter2(requireContext(), foods)

                    binding.recyclerView.adapter = adapter

                    binding.recyclerView.addItemDecoration(
                        DividerItemDecoration(
                            requireContext(),
                            LinearLayoutManager.VERTICAL
                        )
                    )
                }
            }

            override fun onFailure(call: Call<List<FoodInfo?>?>, t: Throwable) {
                // 호출 실패 시 처리합니다.
            }
        })
        /*============================공공 데이터====================================*/


        return binding.root
    }
}