package com.flowerish.cookla.viewModels

import androidx.lifecycle.*
import com.flowerish.cookla.network.MarketFilter
import com.flowerish.cookla.repository.FridgeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PriceViewModel @Inject constructor(private val repository: FridgeRepository) : ViewModel(){

    val agricultureList = repository.agricultureList

    private val _marketFilter = MutableLiveData<MarketFilter>()
    val marketFilter : LiveData<MarketFilter>
        get() = _marketFilter

    val agricultureName = MutableLiveData<String>()

    val combinedData = CombinedMarketCrop(_marketFilter, agricultureName)

    fun refreshAgricultureList(marketFilter: MarketFilter?, agricultureName: String?){
        viewModelScope.launch {
            repository.refreshAgricultureList(marketFilter, agricultureName)
        }
    }

    fun setMarketFilter(marketFilter: MarketFilter){
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
                if( it != null ) this.marketFilter = it
                value = Pair(this.marketFilter, this.cropName)
            }

            addSource(cropName) {
                if( it != null ) this.cropName = it
                value = Pair(this.marketFilter, this.cropName)
            }
        }
    }

}