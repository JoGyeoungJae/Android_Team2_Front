package com.example.frontend.service

import com.example.frontend.dto.City
import com.example.frontend.dto.FoodInfo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface CityService {
    @GET("/cityfindAll")
    fun getcityList(): Call<List<City?>?>?
}