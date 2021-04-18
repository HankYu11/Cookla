package com.example.fridge.price

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PriceViewModel : ViewModel(){
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    private val _lstAgriculture = MutableLiveData<List<AgricultureProperty>>()
    val lstAgriculture : LiveData<List<AgricultureProperty>>
        get() = _lstAgriculture

    fun getAgricultureProperties(){
        coroutineScope.launch {
            val getPropertiesDeferred = AgricultureApi.retrofitService.getProperties()
            try {
                val listResult = getPropertiesDeferred.await()
                _lstAgriculture.value = listResult
            } catch (e: Exception) {
                println("Null !!!!!!!!!!!!!!!!!!")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}