package com.example.frontend.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontend.R
import com.example.frontend.databinding.FragmentMainTwoBinding
import com.example.frontend.restaurant.MyApplication
import com.example.frontend.restaurant.PageListModel
import com.example.myexample.recycler.MyAdapter2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val serviceKey =
            "Jo/F8Pswa2Ul50H9F2/iWeQFCrrF2CuVqL+0cEJJVXlLPLQ0TCqZta52lfANIq63d6lc/4VTIeQoIYEFR84pDQ=="
        val resultType = "json"
        binding = FragmentMainTwoBinding.inflate(inflater, container, false)
        /*============================공공 데이터====================================*/
        val applicationContext = requireActivity().applicationContext
        val networkService = (applicationContext as MyApplication).networkService
        val userListCall = networkService.getList(serviceKey, 1, 10, resultType)
        Log.d("lsy", "url:" + userListCall.request().url().toString())

        userListCall.enqueue(object : Callback<PageListModel> {
            override fun onResponse(call: Call<PageListModel>, response: Response<PageListModel>) {

                Log.d("lsy", "실행 여부 확인. userListCall.enqueue")
                val userList = response.body()
                Log.d("lsy", "userList data 값 : ${userList?.getFoodKr?.item}")
                Log.d("lsy", "userList data 갯수 : ${userList?.getFoodKr?.item?.size}")

                val adapter = MyAdapter2(requireContext(), userList?.getFoodKr?.item)

                binding.recyclerView.adapter = adapter

                binding.recyclerView.addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        LinearLayoutManager.VERTICAL
                    )
                )
            }

            override fun onFailure(call: retrofit2.Call<PageListModel>, t: Throwable) {
                Log.d("lsy", "fail")
                call.cancel()
            }
        })
        /*============================공공 데이터====================================*/


        return binding.root
    }
}