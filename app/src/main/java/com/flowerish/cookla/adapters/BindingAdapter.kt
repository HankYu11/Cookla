package com.flowerish.cookla.adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flowerish.cookla.R
import com.flowerish.cookla.domain.BuyingIngredient
import com.flowerish.cookla.domain.DayWithIngredients
import com.flowerish.cookla.domain.Ingredient
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.*

@BindingAdapter("dayListData")
fun submitDayList(recyclerView: RecyclerView, data: List<DayWithIngredients>?) {
    val adapter = recyclerView.adapter as DayAdapter
    adapter.submitList(data)
}

@BindingAdapter("ingredientListData")
fun submitIngredientList(recyclerView: RecyclerView, data: List<Ingredient>?) {
    val adapter = recyclerView.adapter as StockAdapter
    adapter.submitList(data)
}

@BindingAdapter("buyingListData")
fun submitBuyingList(recyclerView: RecyclerView, data: List<BuyingIngredient>?) {
    val adapter = recyclerView.adapter as BuyingAdapter
    adapter.submitList(data)
}

@BindingAdapter("dateDay")
fun setDateDay(textView: TextView, date: LocalDate) {
    textView.text = textView.context.getString(
        R.string.date_day, date.dayOfMonth,
        date.plusDays(6).dayOfMonth
    )
}

@BindingAdapter("dayOfWeekMonth")
fun setDayOfWeekMonth(textView: TextView, day: DayWithIngredients) {
    textView.text = textView.context.getString(
        R.string.day_of_week_month,
        day.date.dayOfMonth,
        day.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
    )
}

@BindingAdapter("ingredientText")
fun setIngredientText(textView: TextView, ingredient: Ingredient) {
    textView.text = textView.context.getString(
        R.string.stockIngredient, ingredient.name, ingredient.portions,
        LocalDate.now().until(ingredient.expiringDate, ChronoUnit.DAYS)
    )
}

@BindingAdapter("buyingIngredientText")
fun setIngredientText(textView: TextView, ingredient: BuyingIngredient) {
    textView.text = "name: ${ingredient.name}, portion: ${ingredient.portion}"
}

@BindingAdapter("menuYearMonth")
fun TextView.setMenuYearMonth(weekList: List<DayWithIngredients>){
    text = context.getString(R.string.year_month, weekList[0].date.year, weekList[0].date.month)
}

@BindingAdapter("weekFirstAndLastDay")
fun TextView.setWeekFirstAndLastDay(weekList: List<DayWithIngredients?>){
    text = context.getString(R.string.date_day,weekList[0]?.date?.dayOfMonth, weekList[6]?.date?.dayOfMonth)
}