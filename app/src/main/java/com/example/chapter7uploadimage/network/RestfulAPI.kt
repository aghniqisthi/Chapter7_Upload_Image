package com.example.chapter7uploadimage.network

import com.example.chapter7uploadimage.model.ResponseDataUserItem
import com.google.gson.annotations.SerializedName
import okhttp3.Address
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface RestfulAPI {
    @GET("auth/user")
    fun getAllUser(): Call<List<ResponseDataUserItem>>

    @POST("auth/register")
    @Multipart
    fun addUser(
        @Part("email") email: RequestBody,
        @Part("password") password : RequestBody,
        @Part("full_name") fullName : RequestBody,
        @Part("address") address : RequestBody,
        @Part imageUrl : MultipartBody.Part
    ): Call<ResponseDataUserItem>
}