package com.example.frontend.dto

import com.google.gson.annotations.SerializedName
data class FoodInfo(
    @SerializedName("rid")
    val rid:Long,
    @SerializedName("rtitle")
    val rtitle:String,
    @SerializedName("rcity")
    val rcity:String,
    @SerializedName("rlat")
    val rlat: Double,
    @SerializedName("rlng")
    val rlng: Double,
    @SerializedName("rtel")
    val rtel: String,
    @SerializedName("rmainimg")
    val rmainimg: String,
    @SerializedName("rinfo")
    val rinfo: String,
    @SerializedName("rtotalstar")
    val rtotalstar: Long,
    @SerializedName("rstaravg")
    val rstaravg: Double,
    @SerializedName("rcount")
    val rcount: Long,
)
