package com.example.fridge.price

import com.squareup.moshi.Json

data class AgricultureProperty(
    @Json(name = "平均價") val avgPrice : String,
    @Json(name = "作物名稱") val name : String
)