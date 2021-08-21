package com.flowerish.cookla.network

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

enum class MarketFilter(val marketName: String?) {
    TAIPEI_TWO("台北二"),
    TAIPEI_MARKET("台北市場"),
    ALL(null)
}

interface ApiService {
    @GET("AgriProductsTransType/")
    fun getPropertiesAsync(
        @Query("MarketName") marketName: String?,
        @Query("CropName") cropName: String?
    ): Deferred<Response>

    companion object {
        const val BASE_URL = "https://data.coa.gov.tw/api/v1/"
    }
}
