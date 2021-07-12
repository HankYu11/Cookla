package com.flowerish.cookla.viewModels

import androidx.lifecycle.*
import androidx.paging.*
import com.flowerish.cookla.database.asDomainAgriculture
import com.flowerish.cookla.domain.Agriculture
import com.flowerish.cookla.network.MarketFilter
import com.flowerish.cookla.network.NetAgriculture
import com.flowerish.cookla.repository.FridgeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PriceViewModel @Inject constructor(private val repository: FridgeRepository) : ViewModel() {

    var currentSearchResult: Flow<PagingData<PriceUiModel>>? = null

    private val _marketFilter = MutableLiveData<MarketFilter>()

    val agricultureName = MutableLiveData<String>()

    val combinedData = CombinedMarketCrop(_marketFilter, agricultureName)

    @ExperimentalPagingApi
    fun getAgriList(
        marketFilter: MarketFilter?,
        agricultureName: String?
    ): Flow<PagingData<PriceUiModel>> {
        val newData = repository.getAgriculture(
            cropName = agricultureName,
            marketName = marketFilter?.marketName
        )
            .map { pagingData -> pagingData.map { PriceUiModel.AgricultureItem(it.asDomainAgriculture()) }}
            .map {
                it.insertSeparators<PriceUiModel.AgricultureItem, PriceUiModel>{ before, after ->
                    if(before == null){
                        return@insertSeparators PriceUiModel.HeaderItem
                    }else{
                        return@insertSeparators null
                    }
                }
            }
        currentSearchResult = newData
        return newData
    }

    fun setMarketFilter(marketFilter: MarketFilter) {
        _marketFilter.value = marketFilter
    }

    class CombinedMarketCrop(
        marketFilter: LiveData<MarketFilter>,
        cropName: LiveData<String>
    ) : MediatorLiveData<Pair<MarketFilter?, String?>>() {

        private var marketFilter: MarketFilter? = null
        private var cropName: String? = null

        init {
            value = Pair(this.marketFilter, this.cropName)

            addSource(marketFilter) {
                if (it != null) this.marketFilter = it
                value = Pair(this.marketFilter, this.cropName)
            }

            addSource(cropName) {
                if (it != null) this.cropName = it
                value = Pair(this.marketFilter, this.cropName)
            }
        }
    }
}

sealed class PriceUiModel{
    object HeaderItem: PriceUiModel()
    data class AgricultureItem(val agriculture: Agriculture): PriceUiModel()
}