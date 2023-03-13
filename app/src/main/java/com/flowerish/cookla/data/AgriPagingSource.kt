package com.flowerish.cookla.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.flowerish.cookla.data.network.ApiService
import com.flowerish.cookla.data.network.asDomainAgriculture
import com.flowerish.cookla.domain.Agriculture
import retrofit2.HttpException
import java.io.IOException

class AgriPagingSource(
    private val service: ApiService,
    private val cropName: String?,
    private val marketName: String?
): PagingSource<Int, Agriculture>() {
    override fun getRefreshKey(state: PagingState<Int, Agriculture>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // Anchor position is the most recently accessed index
            //如果有前一頁，就是前一頁+1，如果沒有前一頁，就是後一頁-1
            //幹嘛不直接回傳state.anchorPosition?
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Agriculture> {
        return try {
            val response = service.getPropertiesAsync(marketName,cropName).await()
            val data = response.dataList.asDomainAgriculture()

            LoadResult.Page(
                data,
                null,
                null
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}