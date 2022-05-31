package com.conamobile.newfoodapp.networking

import com.conamobile.newfoodapp.model.FoodModel
import retrofit2.Call
import retrofit2.http.GET

interface HomeService {
    @GET("food")
    fun loadAllFood(): Call<ArrayList<FoodModel>>
}