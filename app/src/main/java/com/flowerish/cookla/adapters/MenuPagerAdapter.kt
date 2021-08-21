package com.flowerish.cookla.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flowerish.cookla.databinding.PagerItemMenuBinding
import com.flowerish.cookla.domain.DayWithIngredients
import com.flowerish.cookla.repository.FridgeRepository
import com.flowerish.cookla.viewModels.MenuViewModel
import kotlinx.coroutines.launch
import java.time.temporal.WeekFields
import java.util.*
import kotlin.properties.Delegates

class MenuPagerAdapter(val viewModel: MenuViewModel, val updateDayWithIngredients: (weekList: List<DayWithIngredients>, position: Int) -> Unit)
    : ListAdapter<List<DayWithIngredients>, MenuPagerAdapter.MenuViewHolder>(MenuDiffCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {

        return MenuViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel, position)
    }

    override fun onViewAttachedToWindow(holder: MenuViewHolder) {
        super.onViewAttachedToWindow(holder)
        updateDayWithIngredients(holder.holderWeekList, holder.holderPosition)
    }

    class MenuViewHolder(val binding: PagerItemMenuBinding): RecyclerView.ViewHolder(binding.root) {
        lateinit var holderWeekList: List<DayWithIngredients>
        var holderPosition = 0
        fun bind(weekList: List<DayWithIngredients>, viewModel: MenuViewModel, mPosition: Int){
            holderWeekList = weekList
            holderPosition = mPosition
            binding.weekList = weekList
            binding.rvDay.adapter = DayAdapter(viewModel)
        }

        companion object{
            fun from(parent: ViewGroup): MenuViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return MenuViewHolder(
                    PagerItemMenuBinding.inflate(
                        inflater,
                        parent,
                        false
                    )
                )
            }
        }
    }

    companion object{
        object MenuDiffCallBack: DiffUtil.ItemCallback<List<DayWithIngredients>>() {
            override fun areItemsTheSame(
                oldItem: List<DayWithIngredients>,
                newItem: List<DayWithIngredients>
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: List<DayWithIngredients>,
                newItem: List<DayWithIngredients>
            ): Boolean {
                var isAllTheSame = true
                oldItem.forEachIndexed { index, dayWithIngredients ->
                    if(newItem[index] == dayWithIngredients) isAllTheSame = false
                }
                return isAllTheSame
            }
        }
    }
}