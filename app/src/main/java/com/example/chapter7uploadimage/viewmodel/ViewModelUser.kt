package com.example.chapter7uploadimage.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chapter7uploadimage.model.ResponseDataUserItem
import com.example.chapter7uploadimage.network.RetrofitClient
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelUser : ViewModel() {
    lateinit var addLiveDataUser : MutableLiveData<ResponseDataUserItem>

    init {
        addLiveDataUser = MutableLiveData()
    }

    fun postLiveDataUser() : MutableLiveData<ResponseDataUserItem> {
        return addLiveDataUser
    }

    fun postApiUser(email : RequestBody, password:RequestBody, fullName : RequestBody, address : RequestBody, imageUrl : MultipartBody.Part){
        RetrofitClient.instance.addUser(email, password, fullName, address,  imageUrl)
            .enqueue(object : Callback<ResponseDataUserItem> {
                override fun onResponse(call: Call<ResponseDataUserItem>, response: Response<ResponseDataUserItem>) {
                    if (response.isSuccessful){
                        addLiveDataUser.postValue(response.body())
                    }
                    else{
                        addLiveDataUser.postValue(null)
                    }
                }

                override fun onFailure(call: Call<ResponseDataUserItem>, t: Throwable) {
                    addLiveDataUser.postValue(null)
                }
            })
    }
}