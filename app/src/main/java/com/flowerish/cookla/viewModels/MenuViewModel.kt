package com.flowerish.cookla.viewModels

import androidx.lifecycle.*
import com.flowerish.cookla.Event
import com.flowerish.cookla.domain.BuyingIngredient
import com.flowerish.cookla.domain.DayIngredient
import com.flowerish.cookla.domain.DayWithIngredients
import com.flowerish.cookla.repository.FridgeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(private val repository: FridgeRepository) : ViewModel() {
    /**
     * Now, new idea is using three fragments and try to update the list
     */
    private val _pagerWeekList = MutableLiveData<List<List<Calendar>>>()
    val pagerWeekList: LiveData<List<List<Calendar>>> = _pagerWeekList

    private val _date = MutableLiveData(LocalDate.now())
    val date: LiveData<LocalDate>
        get() = _date

    private val _weekList = MutableLiveData<List<DayWithIngredients>>()
    val weekList: LiveData<List<DayWithIngredients>>
        get() = _weekList

    private val _popupAdd = MutableLiveData<Event<LocalDate>>()
    val popupAdd: LiveData<Event<LocalDate>>
        get() = _popupAdd

    init {
        viewModelScope.launch {
            refreshWeekList()
            _pagerWeekList.value = generateListOfWeek()
        }
    }

    //從2007年1月開始 因為剛好1/1星期一
    private fun generateListOfWeek(): List<List<Calendar>>{
        val calendar = Calendar.getInstance()
        calendar.set(2007,1,1)
        val sp = SimpleDateFormat("yyyy-MM-dd")

        val mList = mutableListOf<List<Calendar>>()
        //1601周約30年
        for (i in 0..1600){
            Timber.d(sp.format(calendar.time))
            mList.add(generateWeek(calendar))
            calendar.add(Calendar.DAY_OF_MONTH, 7)
        }
        return mList
    }

    private fun generateWeek(calendar: Calendar): List<Calendar>{
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        val weekList = mutableListOf<Calendar>()
        for (i in 0..6) {
            weekList.add(calendar)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return weekList
    }

    fun adjustDate(isNext: Boolean) {
        if (isNext) _date.value = _date.value!!.plusDays(7)
        else _date.value = _date.value!!.minusDays(7)
        viewModelScope.launch {
            refreshWeekList()
        }
    }

    fun onMenuAddClick(date: LocalDate) {
        _popupAdd.value = Event(date)
    }

    fun onPopupAddClick(date: LocalDate, name: String, portions: Int) {
        viewModelScope.launch {
            if(repository.getIngredient(name) == null){
                repository.addBuyingIngredient(BuyingIngredient(name,portions))
            }
            repository.getIngredient(name)?.let { ingredient ->
                if(ingredient.portions < portions){
                    repository.addBuyingIngredient(BuyingIngredient(name, portions - ingredient.portions))
                }
            }
            repository.addDayIngredient(date, name, portions, false)
            refreshWeekList()
        }
    }

    fun onDayIngredientClick(dayIngredient: DayIngredient) {
        viewModelScope.launch {
            val ingredient = repository.getIngredient(dayIngredient.name)
            ingredient?.let {
                if (!dayIngredient.isChecked) {
                    it.apply {
                        repository.addIngredient(
                            name,
                            portions - dayIngredient.portions,
                            expiringDate
                        )
                    }
                } else {
                    repository.addIngredient(
                        it.name,
                        it.portions + dayIngredient.portions,
                        it.expiringDate
                    )
                }
            }
            dayIngredient.isChecked = dayIngredient.isChecked.not()
            repository.updateDayIngredient(dayIngredient)
        }
    }

    private suspend fun refreshWeekList() {
        val weekList = mutableListOf<DayWithIngredients>()
        for (i in 0..6) {
            val date = _date.value!!.plusDays(i.toLong())
            val dailyList = repository.getDayIngredientListInDay(date)
            if (dailyList.isNotEmpty()) {
                weekList.add(DayWithIngredients(date, dailyList))
            } else {
                weekList.add(DayWithIngredients(date))
            }
        }
        _weekList.value = weekList
    }

}