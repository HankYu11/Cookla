package com.flowerish.cookla.repository


import androidx.lifecycle.Transformations
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.flowerish.cookla.database.*
import com.flowerish.cookla.domain.*
import com.flowerish.cookla.network.ApiService
import com.flowerish.cookla.network.asDatabaseAgriculture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject


class FridgeRepository @Inject constructor(
    private val dao: FridgeDao,
    private val service: ApiService
) {
    /**
     * Ingredient
     */
    val ingredientList = Transformations.map(dao.getAllIngredients()) {
        it.asDomainIngredientList()
    }

    suspend fun getIngredient(name: String): Ingredient? {
        return withContext(Dispatchers.IO) {
            dao.getIngredient(name)?.asDomainIngredient()
        }

    }

    suspend fun addIngredient(name: String, portions: Int, expiringDate: LocalDate) {
        withContext(Dispatchers.IO) {
            dao.insertIngredient(DatabaseIngredient(name, portions, expiringDate))
        }
    }

    /**
     * DayIngredient
     */
    suspend fun getDayIngredientListInDay(useDate: LocalDate): List<DayIngredient> {
        return withContext(Dispatchers.IO) {
            dao.getDayIngredientListInDay(useDate).map {
                it.asDomainDayIngredient()
            }
        }
    }

    suspend fun addDayIngredient(
        useDate: LocalDate,
        name: String,
        portions: Int,
        isChecked: Boolean
    ) {
        withContext(Dispatchers.IO) {
            dao.insertDayIngredient(
                DatabaseDayIngredient(
                    useDate,
                    name,
                    portions,
                    isChecked
                )
            )
        }
    }

    suspend fun updateDayIngredient(dayIngredient: DayIngredient) {
        withContext(Dispatchers.IO) {
            dao.updateDayIngredient(dayIngredient.asDatabaseDayIngredient())
        }
    }

    /**
     * BuyingIngredient
     */
    suspend fun addBuyingIngredient(buyingIngredient: BuyingIngredient) {
        withContext(Dispatchers.IO) {
            dao.insertBuyingIngredient(buyingIngredient.asDatabaseBuyingIngredient())
        }
    }

    suspend fun deleteBuyingIngredient(buyingIngredient: BuyingIngredient) {
        withContext(Dispatchers.IO) {
            dao.deleteBuyingIngredient(buyingIngredient.asDatabaseBuyingIngredient())
        }
    }

    suspend fun getBuyingIngredient(name: String): BuyingIngredient? {
        return withContext(Dispatchers.IO) {
            dao.getBuyingIngredient(name)?.asDomainBuyingIngredient()
        }
    }

    val buyingIngredientList = Transformations.map(dao.getBuyingList()) {
        it.asDomainBuyingIngredientList()
    }


    /**
     * Agriculture
     */
    val allAgriculture = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { dao.getAllAgriculture() }
    ).liveData


    suspend fun refreshAgriculture(marketName: String?, cropName: String?) {
        withContext(Dispatchers.IO){
            dao.deleteAllAgriculture()
            val response = service.getPropertiesAsync(marketName, cropName).await()
            dao.insertAllAgriculture(*response.dataList.filter {
                it.name != "休市"
            }.asDatabaseAgriculture())
        }
    }
}