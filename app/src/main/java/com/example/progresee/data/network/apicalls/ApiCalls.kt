package com.example.progresee.data.network.apicalls

import com.example.progresee.beans.User
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface ApiCalls {

    @GET("/user/getCurrentUser")
    fun getCurrentUserAsync(@Header("Authorization") token:String?):Deferred<Response<User>>
}