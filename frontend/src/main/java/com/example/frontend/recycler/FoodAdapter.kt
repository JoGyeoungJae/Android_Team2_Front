package com.example.frontend.recycler

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
import com.example.frontend.dto.FoodInfo
import com.example.frontend.main.ItemActivity


//부산맛집
class MyViewHolder2(val binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root)

class MyAdapter2(val context: Context, val datas: List<FoodInfo?>?): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemCount(): Int{
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = MyViewHolder2(ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as MyViewHolder2).binding

        //도보 여행
        val food = datas?.get(position)
        binding.firstNameView.text = food?.rtitle
        val urlImg = food?.rmainimg
        binding.contactView.text = food?.rtel

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
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ItemActivity::class.java)

            intent.putExtra("rid",food?.rid)
            intent.putExtra("rtitle",food?.rtitle)
            intent.putExtra("rcity",food?.rcity)
            intent.putExtra("rlat",food?.rlat)
            intent.putExtra("rlng",food?.rlng)
            intent.putExtra("rtel",food?.rtel)
            intent.putExtra("rinfo",food?.rinfo)
            intent.putExtra("rmainimg",food?.rmainimg)

            context.startActivity(intent)
        }

    }

}