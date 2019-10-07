package com.example.progresee.data.network

import com.example.progresee.data.network.apicalls.ApiCalls
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class ApiService {

    private var BASE_URL="http://192.168.0.5:5000"

    private val client = OkHttpClient().newBuilder()
        .build()

    fun retrofit():ApiCalls {
        val retrofit=Retrofit.Builder().baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
        return  retrofit.create(ApiCalls::class.java)
    }
}