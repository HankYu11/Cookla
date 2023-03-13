package com.flowerish.cookla.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flowerish.cookla.databinding.RecyclerItemDayBinding
import com.flowerish.cookla.domain.DayWithIngredients
import com.flowerish.cookla.viewModels.MenuViewModel

class DayAdapter(val viewModel: MenuViewModel) : ListAdapter<DayWithIngredients, DayAdapter.DayViewHolder>(
    DayDiffUtil()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        return DayViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel)
    }

    class DayViewHolder(val binding: RecyclerItemDayBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dayWithIngredients: DayWithIngredients,
            viewModel: MenuViewModel
        ){
            binding.dayWithIngredients = dayWithIngredients
            val adapter = DayIngredientAdapter(viewModel)
            binding.rvIngredient.adapter = adapter
            adapter.submitList(dayWithIngredients.ingredientList)
            binding.viewModel = viewModel
        }
        companion object{
            fun from(parent: ViewGroup): DayViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerItemDayBinding.inflate(layoutInflater,parent,false)
                return DayViewHolder(binding)
            }
        }
    }

    class DayDiffUtil : DiffUtil.ItemCallback<DayWithIngredients>() {
        override fun areItemsTheSame(oldItem: DayWithIngredients, newItem: DayWithIngredients): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: DayWithIngredients, newItem: DayWithIngredients): Boolean {
            return oldItem.date == newItem.date && oldItem.ingredientList == newItem.ingredientList
        }

    }
}