package com.flowerish.cookla.data

import androidx.paging.*
import androidx.room.withTransaction
import com.flowerish.cookla.database.DatabaseAgriculture
import com.flowerish.cookla.database.FridgeDao
import com.flowerish.cookla.database.FridgeDatabase
import com.flowerish.cookla.domain.Agriculture
import com.flowerish.cookla.network.ApiService
import com.flowerish.cookla.network.asDatabaseAgriculture
import com.flowerish.cookla.network.asDomainAgriculture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class PriceRemoteMediator(
    private val dao: FridgeDao,
    private val service: ApiService,
    private val marketName: String?,
    private val cropName: String?
): RemoteMediator<Int, DatabaseAgriculture>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DatabaseAgriculture>
    ): MediatorResult {
        return try {
            val response = service.getPropertiesAsync(marketName,cropName).await()
            withContext(Dispatchers.IO) {
                if (loadType == LoadType.REFRESH) dao.deleteAllAgriculture()
                dao.insertAllAgriculture(*response.dataList.filter {
                    it.name != "休市"
                }.asDatabaseAgriculture())
            }
            MediatorResult.Success(endOfPaginationReached = true)
        }catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }

    override suspend fun initialize(): InitializeAction {
        return super.initialize()
    }
}