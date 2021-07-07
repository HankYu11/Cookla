package com.flowerish.cookla.viewModels

import androidx.lifecycle.ViewModel
import com.flowerish.cookla.repository.FridgeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BuyingViewModel @Inject constructor(repository: FridgeRepository): ViewModel() {
    val buyingIngredientList = repository.buyingIngredientList
}