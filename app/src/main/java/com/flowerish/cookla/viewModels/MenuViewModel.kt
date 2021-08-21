package com.flowerish.cookla.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowerish.cookla.Event
import com.flowerish.cookla.adapters.DayAdapter
import com.flowerish.cookla.domain.BuyingIngredient
import com.flowerish.cookla.domain.DayIngredient
import com.flowerish.cookla.domain.DayWithIngredients
import com.flowerish.cookla.repository.FridgeRepository
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
class MenuViewModel @Inject constructor(val repository: FridgeRepository) : ViewModel() {

    private val _pagerWeekList = MutableLiveData<MutableList<List<DayWithIngredients>>>()
    val pagerWeekList: LiveData<MutableList<List<DayWithIngredients>>> = _pagerWeekList


    private val _date = MutableLiveData(LocalDate.now())
    val date: LiveData<LocalDate>
        get() = _date

    private val _popupAdd = MutableLiveData<Event<LocalDate>>()
    val popupAdd: LiveData<Event<LocalDate>>
        get() = _popupAdd

    sealed class MenuEvent {
        object RefreshData : MenuEvent()
        class ScrollToCurrentDay(val currentDayPosition: Int) : MenuEvent()
    }

    private val eventChannel = Channel<MenuEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()


    init {
        viewModelScope.launch {
            generateListOfWeek()
        }
    }

    fun updateDayWithIngredients(weekList: List<DayWithIngredients>, position: Int) {
        viewModelScope.launch {
            val newWeekList = weekList.map {
                val dayWithIngredients = repository.getDayIngredientListInDay(it.date)
                if (dayWithIngredients.isNullOrEmpty()) {
                    it
                } else {
                    DayWithIngredients(it.date, dayWithIngredients)
                }
            }
            val test = _pagerWeekList.value?.apply {
                this[position] = newWeekList
            }
            _pagerWeekList.value = test!!
        }
    }

    //2007/1/1星期一，改成2020開始不然讀太久
    private suspend fun generateListOfWeek() {
        var calendar = LocalDate.of(2020, 1, 1)
        val mList = mutableListOf<List<DayWithIngredients>>()
        var currentDayPosition = 0
        //1601周約30年
        for (i in 0..300) {
            if (calendar.toEpochDay() == LocalDate.now().with(DayOfWeek.WEDNESDAY).toEpochDay()) {
                currentDayPosition = i
            }
            mList.add(generateWeek(calendar))
            calendar = calendar.plusDays(7L)
        }
        _pagerWeekList.value = mList
        eventChannel.send(MenuEvent.ScrollToCurrentDay(currentDayPosition))
    }

    private fun generateWeek(date: LocalDate): List<DayWithIngredients> {
        val weekList = mutableListOf<DayWithIngredients>()
        for (i in 1..7) {
            val mDate = date.with(WeekFields.of(Locale.FRANCE).dayOfWeek(), i.toLong())
            weekList.add(DayWithIngredients(mDate))
        }
        return weekList
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

}