package com.flowerish.cookla.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.flowerish.cookla.domain.Agriculture
import com.flowerish.cookla.domain.BuyingIngredient
import com.flowerish.cookla.domain.DayIngredient
import com.flowerish.cookla.domain.Ingredient
import java.time.LocalDate

@Entity(tableName = "Ingredients", primaryKeys = ["name","expiringDate"])
data class DatabaseIngredient(
    val name: String,
    val portions: Int,
    val expiringDate: LocalDate
)

@Entity(tableName = "DayIngredients")
data class DatabaseDayIngredient(
    val useDate: LocalDate,
    val ingredientName: String,
    val ingredientPortion: Int,
    val isChecked: Boolean,
    @PrimaryKey(autoGenerate = true)
    val dayIngredientId: Int = 0
)

@Entity(tableName = "BuyingList")
data class DatabaseBuyingIngredient(
    @PrimaryKey
    val name: String,
    val portion: Int,
)

@Entity(tableName = "Agriculture")
data class DatabaseAgriculture(
    val avgPrice: String,
    val name: String,
    val marketName: String,
    @PrimaryKey(autoGenerate = true)
    val agricultureId: Int = 0
)

fun DatabaseIngredient.asDomainIngredient(): Ingredient{
    return Ingredient(name, portions, expiringDate)
}

fun List<DatabaseIngredient>.asDomainIngredientList(): List<Ingredient>{
    return map{
        it.asDomainIngredient()
    }
}

fun DatabaseDayIngredient.asDomainDayIngredient(): DayIngredient{
    return DayIngredient(useDate, ingredientName, ingredientPortion, isChecked, dayIngredientId)
}

fun List<DatabaseAgriculture>.asDomainAgricultureList(): List<Agriculture> {
    return map{
        Agriculture(it.avgPrice, it.name, it.marketName)
    }
}

fun DatabaseBuyingIngredient.asDomainBuyingIngredient(): BuyingIngredient{
    return BuyingIngredient(name, portion)
}

fun List<DatabaseBuyingIngredient>.asDomainBuyingIngredientList(): List<BuyingIngredient>{
    return map{
        it.asDomainBuyingIngredient()
    }
}

fun DatabaseAgriculture.asDomainAgriculture(): Agriculture{
    return Agriculture(avgPrice, name, marketName)
}


/**
 * one to many relation (not in use)
 */
//@Entity(primaryKeys = ["date", "name"])
//data class DayIngredientCrossRef(
//    @ColumnInfo(name = "date", index = true)
//    val date: Long,
//    @ColumnInfo(name = "name", index = true)
//    val name: String
//)
//
//data class DayWithIngredients(
//    @Embedded val databaseDay: DatabaseDay,
//    @Relation(
//        parentColumn = "date",
//        entityColumn = "name",
//        associateBy = Junction(DayIngredientCrossRef::class)
//    )
//    val databaseIngredientList: List<DatabaseIngredient>
//)
//
////在哪幾天有用到這個食材(目前用不到)
//data class IngredientInDays(
//    @Embedded val databaseIngredient: DatabaseIngredient,
//    @Relation(
//        parentColumn = "name",
//        entityColumn = "date",
//        associateBy = Junction(DayIngredientCrossRef::class)
//    )
//    val databaseDayList: List<DatabaseDay>
//)