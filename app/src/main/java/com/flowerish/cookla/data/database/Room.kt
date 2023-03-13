package com.flowerish.cookla.data.database

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import java.time.LocalDate

/**
 * crossRef is for one to many relation
 */
@Database(entities = [DatabaseIngredient::class, DatabaseDayIngredient::class, DatabaseAgriculture::class, DatabaseBuyingIngredient::class], version = 1, exportSchema = false)
@TypeConverters(MyConverters::class)
abstract class FridgeDatabase : RoomDatabase(){
    abstract fun getDao(): FridgeDao
}

class MyConverters{
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofEpochDay(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }
}

@Dao
interface FridgeDao{
    /**
     * Ingredient
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIngredient(ingredient: DatabaseIngredient)

    @Query("select * from ingredients where name = :name")
    fun getIngredient(name: String): DatabaseIngredient?

    @Query("select * from ingredients")
    fun getAllIngredients(): LiveData<List<DatabaseIngredient>>

    /**
     * DayIngredient
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDayIngredient(dayIngredient: DatabaseDayIngredient)

    @Query("select * from dayingredients where useDate = :useDate")
    fun getDayIngredientListInDay(useDate: LocalDate): List<DatabaseDayIngredient>

    @Update
    fun updateDayIngredient(databaseDayIngredient: DatabaseDayIngredient)

    /**
     * BuyingIngredient
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBuyingIngredient(buyingIngredient: DatabaseBuyingIngredient)

    @Query("select * from buyinglist")
    fun getBuyingList(): LiveData<List<DatabaseBuyingIngredient>>

    @Delete
    fun deleteBuyingIngredient(buyingIngredient: DatabaseBuyingIngredient)

    @Query("select * from buyinglist where name = :name")
    fun getBuyingIngredient(name: String): DatabaseBuyingIngredient?

    /**
     * Agriculture
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllAgriculture(vararg agriculture: DatabaseAgriculture)

    @Query("delete from agriculture")
    fun deleteAllAgriculture()

    @Query("select * from agriculture")
    fun getAllAgriculture(): PagingSource<Int, DatabaseAgriculture>

}