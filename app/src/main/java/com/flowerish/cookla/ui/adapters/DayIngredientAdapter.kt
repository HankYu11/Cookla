package com.flowerish.cookla.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flowerish.cookla.databinding.RecyclerItemIngredientBinding
import com.flowerish.cookla.domain.DayIngredient
import com.flowerish.cookla.viewModels.MenuViewModel

class DayIngredientAdapter(val viewModel: MenuViewModel): ListAdapter<DayIngredient, DayIngredientAdapter.IngredientViewHolder>(
    IngredientDiffUtil()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        return IngredientViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(getItem(position),viewModel)
    }

    class IngredientViewHolder(val binding: RecyclerItemIngredientBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(
            dayIngredient: DayIngredient,
            viewModel: MenuViewModel
        ){
            binding.ingredient = dayIngredient
            if(dayIngredient.isChecked) binding.ivCheck.visibility = View.VISIBLE
            else binding.ivCheck.visibility = View.GONE
            binding.cvDayIngredient.setOnClickListener {
                if(dayIngredient.isChecked) binding.ivCheck.visibility = View.GONE
                else binding.ivCheck.visibility = View.VISIBLE
                viewModel.onDayIngredientClick(dayIngredient)
            }
        }

        companion object{
            fun from(parent: ViewGroup): IngredientViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerItemIngredientBinding.inflate(layoutInflater,parent,false)
                return IngredientViewHolder(binding)
            }
        }
    }

    class IngredientDiffUtil : DiffUtil.ItemCallback<DayIngredient>() {
        override fun areItemsTheSame(oldItem: DayIngredient, newItem: DayIngredient): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: DayIngredient, newItem: DayIngredient): Boolean {
            return oldItem.name == newItem.name && oldItem.portions == newItem.portions
        }

    }
}