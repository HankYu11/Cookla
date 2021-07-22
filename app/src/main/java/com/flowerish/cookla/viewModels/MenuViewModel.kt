package com.flowerish.cookla.viewModels

import androidx.lifecycle.*
import androidx.lifecycle.map
import com.flowerish.cookla.Event
import com.flowerish.cookla.domain.BuyingIngredient
import com.flowerish.cookla.domain.DayIngredient
import com.flowerish.cookla.domain.DayWithIngredients
import com.flowerish.cookla.repository.FridgeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(private val repository: FridgeRepository) : ViewModel() {
    /**
     * Now, new idea is using three fragments and try to update the list
     */
    private val currentWeekCalendar = MutableStateFlow(Calendar.getInstance())

    private val prevWeekCalendar: StateFlow<Calendar>
        get() = currentWeekCalendar.let {
            it.value.add(Calendar.DAY_OF_MONTH, -7)
            it
        }
    private val nextWeekCalendar: StateFlow<Calendar>
        get() = currentWeekCalendar.let {
            it.value.add(Calendar.DAY_OF_MONTH, 7)
            it
        }

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
        }
    }

    fun addWeek(){
        val currentWeekCal = currentWeekCalendar.value
        currentWeekCal.add(Calendar.DAY_OF_MONTH, 7)
        currentWeekCalendar.value = currentWeekCal
        val format: DateFormat = SimpleDateFormat("MM/dd/yyyy")
        Timber.d("current: ${format.format(currentWeekCalendar.value.time)} prev: ${format.format(prevWeekCalendar.value.time)}")
    }

    fun getWeek(){
        val format: DateFormat = SimpleDateFormat("MM/dd/yyyy")
        val calendar: Calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        val days = arrayOfNulls<String>(7)
        for (i in 0..6) {
            days[i] = format.format(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
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