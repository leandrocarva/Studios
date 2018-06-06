package com.example.carvalho.studios.controller

import com.example.carvalho.studios.api.StudioRest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object StudioService {
    private val BASE_URL = "http://www2.gmdlogistica.com.br:8085/studioAPI/"
    public var service: StudioRest

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        service = retrofit.create(StudioRest::class.java)
    }
}