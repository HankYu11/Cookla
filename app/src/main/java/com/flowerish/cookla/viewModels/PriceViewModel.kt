package com.flowerish.cookla.viewModels

import androidx.lifecycle.*
import androidx.paging.insertSeparators
import androidx.paging.map
import com.flowerish.cookla.data.database.asDomainAgriculture
import com.flowerish.cookla.data.network.MarketFilter
import com.flowerish.cookla.data.repository.FridgeRepository
import com.flowerish.cookla.domain.Agriculture
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PriceViewModel @Inject constructor(private val repository: FridgeRepository) : ViewModel() {

    private val marketFilter = MutableLiveData<MarketFilter>()

    val agricultureName = MutableLiveData<String>()

    val searchCondition = CombinedMarketCrop(marketFilter, agricultureName)

    val agricultureList = repository.allAgriculture
        .map { pagingData -> pagingData.map { PriceUiModel.AgricultureItem(it.asDomainAgriculture()) } }
        .map {
            it.insertSeparators { before, after ->
                if (before == null) {
                    return@insertSeparators PriceUiModel.HeaderItem
                } else {
                    return@insertSeparators null
                }
            }
        }

    fun search(marketFilter: MarketFilter?,
               agricultureName: String?){
        viewModelScope.launch {
            repository.refreshAgriculture(marketFilter?.marketName, agricultureName)
        }
    }

    fun setMarketFilter(marketFilter: MarketFilter) {
        this.marketFilter.value = marketFilter
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

sealed class PriceUiModel {
    object HeaderItem : PriceUiModel()
    data class AgricultureItem(val agriculture: Agriculture) : PriceUiModel()
}