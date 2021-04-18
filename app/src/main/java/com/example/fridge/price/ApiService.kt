package com.example.fridge.price

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://data.coa.gov.tw"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface ApiService{
    // /Service/OpenData/FromM/FarmTransData.aspx?$top=1000&$skip=0&StartDate=109.09.22&EndDate=109.09.22&Crop=椰子&Market=台北二
    @GET("/Service/OpenData/FromM/FarmTransData.aspx?\$filter={\\u7a2e\\u985e\\u4ee3\\u78bc=N05}")
    fun getProperties() : Deferred<List<AgricultureProperty>>
}

object AgricultureApi{
    val retrofitService : ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
