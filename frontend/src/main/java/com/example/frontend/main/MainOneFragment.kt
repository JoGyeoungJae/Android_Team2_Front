package com.example.frontend.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontend.databinding.FragmentMainOneBinding
import com.example.frontend.db.DBConnect
import com.example.frontend.dto.City
import com.example.frontend.dto.FoodInfo
import com.example.frontend.recycler.CityAdapterAdapter2
import com.example.frontend.recycler.MyAdapter2
import com.example.frontend.service.CityService
import com.example.frontend.service.FoodInfoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainOneFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainOneFragment : Fragment() {
    lateinit var binding : FragmentMainOneBinding
    lateinit var cityService : CityService
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainOneBinding.inflate(inflater, container, false)

        val retrofit = DBConnect.retrofit

        cityService = retrofit.create(CityService::class.java)

        val call: Call<List<City?>?>? = cityService.getcityList()
        Log.d("joj",call.toString())
        call?.enqueue(object : Callback<List<City?>?> {
            override fun onResponse(call: Call<List<City?>?>, response: Response<List<City?>?>) {
                if (response.isSuccessful) {
                    val citys = response.body()
                    // 여기서 받아온 데이터(items)를 처리합니다.
                    val adapter = CityAdapterAdapter2(requireContext(), citys)

                    binding.recyclerViewone.adapter = adapter

                }
            }

            override fun onFailure(call: Call<List<City?>?>, t: Throwable) {
                // 호출 실패 시 처리합니다.
            }
        })


        return binding.root
    }

}