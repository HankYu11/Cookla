package com.flowerish.cookla.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowerish.cookla.data.repository.FridgeRepository
import com.flowerish.cookla.domain.BuyingIngredient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuyingViewModel @Inject constructor(val repository: FridgeRepository): ViewModel() {

    val buyingIngredientList = repository.buyingIngredientList

    private val eventChannel = Channel<BuyingEvent>()
    val eventsFlow = eventChannel.receiveAsFlow()

    fun onAddClick(){
        viewModelScope.launch {
            eventChannel.send(BuyingEvent.Add)
        }
    }

    fun addBuyingIngredient(name: String, portion: Int){
        viewModelScope.launch {
            repository.addBuyingIngredient(BuyingIngredient(name, portion))
        }
    }
}

sealed class BuyingEvent{
    object Add : BuyingEvent()
}