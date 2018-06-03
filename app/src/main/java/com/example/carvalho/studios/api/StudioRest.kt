package com.example.carvalho.studios.api


import com.example.carvalho.studios.model.Studio
import retrofit2.Call
import retrofit2.http.*

interface StudioRest {

    @GET("studios/")
    fun getStudios() : Call<List<Studio>>
}