package com.flowerish.cookla.network

import com.flowerish.cookla.database.DatabaseAgriculture
import com.flowerish.cookla.domain.Agriculture
import com.squareup.moshi.Json

data class Response(
    @Json(name = "Data") val dataList: List<NetAgriculture>
)

data class NetAgriculture(
    @Json(name = "Avg_Price") val avgPrice : String,
    @Json(name = "CropName") val name : String,
    @Json(name = "MarketName") val marketName: String
)

fun List<NetAgriculture>.asDatabaseAgriculture(): Array<DatabaseAgriculture>{
    return map {
        DatabaseAgriculture(it.avgPrice, it.name, it.marketName)
    }.toTypedArray()
}

fun List<NetAgriculture>.asDomainAgriculture(): List<Agriculture>{
    return map {
        Agriculture(it.avgPrice, it.name, it.marketName)
    }
}