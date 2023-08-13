package com.example.frontend.service

import com.example.frontend.dto.FoodInfo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface FoodInfoService {
    @GET("/foodfindAll")
    fun getFoodInfoList(): Call<List<FoodInfo?>?>?

    @POST("/postFoodInfo")
    fun postFoodInfo(@Body foodInfo: FoodInfo): Call<FoodInfo>
    @POST("/postFoodInfodelete")
    fun postFoodInfodelete(@Body foodInfo: FoodInfo): Call<FoodInfo>
}