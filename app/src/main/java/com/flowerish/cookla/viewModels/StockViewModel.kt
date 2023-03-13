package com.flowerish.cookla.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowerish.cookla.Event
import com.flowerish.cookla.data.repository.FridgeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(private val repository: FridgeRepository): ViewModel() {

    val ingredientsList = repository.ingredientList

    private val _popupAdd = MutableLiveData<Event<String>>()
    val popupAdd : LiveData<Event<String>>
        get() = _popupAdd

    fun addIngredient(name: String, portions: Int, expiringDate: LocalDate){
        viewModelScope.launch {
            repository.addIngredient(name, portions, expiringDate)
        }
    }

    fun onStockAddClick(){
        _popupAdd.value = Event("")
    }
}