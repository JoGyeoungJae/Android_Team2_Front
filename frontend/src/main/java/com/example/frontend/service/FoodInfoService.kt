package com.example.frontend.service

import com.example.frontend.dto.FoodInfo
import retrofit2.Call
import retrofit2.http.GET


interface FoodInfoService {
    @GET("/foodfindAll")
    fun getFoodInfoList(): Call<List<FoodInfo?>?>?
}