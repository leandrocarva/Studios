package com.example.carvalho.studios.api


import com.example.carvalho.studios.model.Studio
import retrofit2.Call
import retrofit2.http.*

interface StudioRest {

    @GET("studios/")
    fun getStudios() : Call<List<Studio>>

    @POST("studios/")
    fun postStudios(@Body studio: Studio) : Call<Studio>

    @PUT("studios/")
    fun upStudios(@Body studio: Studio) : Call<Studio>

    @DELETE("studios/{id}")
    fun delStudios(@Path("id") id: Int): Call<Studio>
}