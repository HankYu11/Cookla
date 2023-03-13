package com.flowerish.cookla.domain

import com.flowerish.cookla.data.database.DatabaseBuyingIngredient
import com.flowerish.cookla.data.database.DatabaseDayIngredient
import java.time.LocalDate

data class Ingredient(
    val name: String,
    val portions: Int,
    val expiringDate: LocalDate
)

data class DayIngredient(
    val useDate: LocalDate,
    val name: String,
    val portions: Int,
    var isChecked: Boolean,
    val id: Int
)

data class DayWithIngredients(
    val date: LocalDate,
    var ingredientList: List<DayIngredient>? = null
)

data class BuyingIngredient(
    val name: String,
    val portion: Int
)

data class Agriculture(
    val avgPrice: String,
    val name: String,
    val marketName: String
)

fun DayIngredient.asDatabaseDayIngredient(): DatabaseDayIngredient {
    return DatabaseDayIngredient(
        useDate,
        name,
        portions,
        isChecked,
        id
    )
}

fun BuyingIngredient.asDatabaseBuyingIngredient(): DatabaseBuyingIngredient {
    return DatabaseBuyingIngredient(name, portion)
}