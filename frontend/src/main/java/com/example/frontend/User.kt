package com.example.frontend

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id:String,
    @SerializedName("password")
    val password:String,
    @SerializedName("email")
    val email:String,
    @SerializedName("phone")
    val phone: String )