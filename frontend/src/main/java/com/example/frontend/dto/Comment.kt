package com.example.frontend.dto

import com.google.gson.annotations.SerializedName

data class Comment(

    @SerializedName("cmt")
    val cmt:String,
//    val id: String,
//    val username: String,
//    val content: String,
    @SerializedName("cmtTime")
    val timestamp: String,

    @SerializedName("reviewimg")
    val reviewimg: String,
    @SerializedName("uid")
    val uid: String

)
