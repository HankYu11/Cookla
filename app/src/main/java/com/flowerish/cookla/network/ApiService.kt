package com.flowerish.cookla.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://agridata.coa.gov.tw/api/v1/"

enum class MarketFilter(val marketName: String?) {
    TAIPEI_TWO("台北二"),
    TAIPEI_MARKET("台北市場"),
    ALL(null)
}
//
//private val moshi = Moshi.Builder()
//    .add(KotlinJsonAdapterFactory())
//    .build()
//
//private val retrofit = Retrofit.Builder()
//    .addConverterFactory(MoshiConverterFactory.create(moshi))
//    .addCallAdapterFactory(CoroutineCallAdapterFactory())
//    .baseUrl(BASE_URL)
//    .build()

interface ApiService {
    @GET("AgriProductsTransType/")
    fun getPropertiesAsync(
        @Query("MarketName") marketName: String?,
        @Query("CropName") cropName: String?
    ): Deferred<Response>
}

//object AgricultureApi {
//    val retrofitService: ApiService by lazy {
//        retrofit.create(ApiService::class.java)
//    }
//}
