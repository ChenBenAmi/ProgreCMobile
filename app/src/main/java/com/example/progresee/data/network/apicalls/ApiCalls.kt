package com.example.progresee.data.network.apicalls

import com.example.progresee.beans.User
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiCalls {

    @POST("login/google")
     fun login():Deferred<Response<User>>

}