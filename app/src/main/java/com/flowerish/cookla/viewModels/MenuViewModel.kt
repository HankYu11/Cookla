package com.flowerish.cookla.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowerish.cookla.Event
import com.flowerish.cookla.domain.BuyingIngredient
import com.flowerish.cookla.domain.DayIngredient
import com.flowerish.cookla.domain.DayWithIngredients
import com.flowerish.cookla.repository.FridgeRepository
import com.flowerish.cookla.toLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(private val repository: FridgeRepository) : ViewModel() {

    private val _pagerWeekList = MutableLiveData<MutableList<List<DayWithIngredients>>>()
    val pagerWeekList: LiveData<MutableList<List<DayWithIngredients>>> = _pagerWeekList

    var currentDayPosition = 0

    private val _date = MutableLiveData(LocalDate.now())
    val date: LiveData<LocalDate>
        get() = _date

    private val _weekList = MutableLiveData<List<DayWithIngredients>>()
    val weekList: LiveData<List<DayWithIngredients>>
        get() = _weekList

    private val _popupAdd = MutableLiveData<Event<LocalDate>>()
    val popupAdd: LiveData<Event<LocalDate>>
        get() = _popupAdd

    sealed class MenuEvent {
        object RefreshData : MenuEvent()
    }

    private val eventChannel = Channel<MenuEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()


    init {
        viewModelScope.launch {
            generateListOfWeek()
        }
    }

    //2007/1/1星期一，改成2020開始不然讀太久
    private suspend fun generateListOfWeek() {
        var calendar = LocalDate.of(2020,1,1)
        val mList = mutableListOf<List<DayWithIngredients>>()
        //1601周約30年
        for (i in 0..300) {
            if (calendar.toEpochDay() == LocalDate.now().with(DayOfWeek.WEDNESDAY).toEpochDay()){
                currentDayPosition = i
            }
            mList.add(generateWeek(calendar))
            calendar = calendar.plusDays(7L)
        }
        _pagerWeekList.value = mList
    }

    private suspend fun generateWeek(date: LocalDate): List<DayWithIngredients> {
//        calendar.firstDayOfWeek = Calendar.MONDAY
//        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val weekList = mutableListOf<DayWithIngredients>()
        for (i in 1..7) {
            val mDate = date.with(WeekFields.of(Locale.FRANCE).dayOfWeek(),i.toLong())
            val dailyList = repository.getDayIngredientListInDay(mDate)
            if (dailyList.isNotEmpty()) {
                weekList.add(DayWithIngredients(mDate, dailyList))
            } else {
                weekList.add(DayWithIngredients(mDate))
            }
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

    fun onPopupAddClick(date: LocalDate, name: String, portions: Int, currentPage: Int) {
        viewModelScope.launch {
            if (repository.getIngredient(name) == null) {
                repository.addBuyingIngredient(BuyingIngredient(name, portions))
            }
            repository.getIngredient(name)?.let { ingredient ->
                if (ingredient.portions < portions) {
                    repository.addBuyingIngredient(
                        BuyingIngredient(
                            name,
                            portions - ingredient.portions
                        )
                    )
                }
            }
            repository.addDayIngredient(date, name, portions, false)

            val currentDay = _pagerWeekList.value?.get(currentPage)?.get(0)?.date
            _pagerWeekList.value?.removeAt(currentPage)
            _pagerWeekList.value?.add(currentPage, generateWeek(currentDay!!))
            eventChannel.send(MenuEvent.RefreshData)
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