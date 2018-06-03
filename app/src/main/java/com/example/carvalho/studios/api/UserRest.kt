package com.example.carvalho.studios.api

import com.example.carvalho.studios.model.User
import retrofit2.Call
import retrofit2.http.*



interface UserRest {

    @GET("user/{id}")
    fun getUser(@Path("id") id: Int): Call<User>

    @GET("user/")
    fun getUsers(): Call<List<User>>

    @POST("user/")
    fun createUser(@Body user: User) : Call<User>

    @PATCH("user/")
    fun veriUser(@Body user: User) : Call<User>

}