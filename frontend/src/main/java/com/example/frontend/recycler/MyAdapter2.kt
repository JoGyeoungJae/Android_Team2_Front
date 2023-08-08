package com.example.myexample.recycler

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.frontend.databinding.ItemMainBinding
import com.example.frontend.main.ItemActivity
import com.example.frontend.restaurant.ItemModel4


//부산맛집
class MyViewHolder2(val binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root)

class MyAdapter2(val context: Context, val datas: List<ItemModel4>?): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemCount(): Int{
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = MyViewHolder2(ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as MyViewHolder2).binding

        //도보 여행
        val user = datas?.get(position)
        binding.firstNameView.text = user?.TITLE
        val urlImg = user?.MAIN_IMG_NORMAL
        binding.contactView.text = user?.CNTCT_TEL

        Glide.with(context)
            .asBitmap()
            .load(urlImg)
            .into(object : CustomTarget<Bitmap>(200, 200) {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    binding.avatarView.setImageBitmap(resource)
//                    Log.d("lsy", "width : ${resource.width}, height: ${resource.height}")
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    TODO("Not yet implemented")
                }
            })
        val binding2=(holder as MyViewHolder2).binding
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ItemActivity::class.java)
            intent.putExtra("id",user?.UC_SEQ)
            intent.putExtra("TITLE",user?.TITLE)
            intent.putExtra("CNTCT_TEL",user?.CNTCT_TEL)
            intent.putExtra("LAT",user?.LAT)
            intent.putExtra("LNG",user?.LNG)
            intent.putExtra("MAIN_IMG_NORMAL",user?.MAIN_IMG_NORMAL)

            context.startActivity(intent)
        }

    }

}