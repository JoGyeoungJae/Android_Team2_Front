package com.example.frontend.recycler

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.databinding.CityitemMainBinding
import com.example.frontend.dto.City
import com.example.frontend.restaurant.ItemActivity


//부산맛집
class CityAdapterViewHolder2(val binding: CityitemMainBinding): RecyclerView.ViewHolder(binding.root)

class CityAdapterAdapter2(val context: Context, val datas: List<City?>?): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemCount(): Int{
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = CityAdapterViewHolder2(CityitemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as CityAdapterViewHolder2).binding

        //도보 여행
        val city = datas?.get(position)
        binding.city.text = city?.ccity


        holder.itemView.setOnClickListener {
            Log.d("joj", "리사이클러뷰 클릭")
            Log.d("joj", city?.cid.toString())
            Log.d("joj", city?.ccity.toString())

//            val intent = Intent(context, ItemActivity::class.java)
//
//            intent.putExtra("rid",food?.rid)
//            intent.putExtra("rtitle",food?.rtitle)
//            context.startActivity(intent)
        }
    }

}