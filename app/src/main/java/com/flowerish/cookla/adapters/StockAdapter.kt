package com.flowerish.cookla.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flowerish.cookla.databinding.RecyclerItemStockBinding
import com.flowerish.cookla.domain.Ingredient

class StockAdapter : ListAdapter<Ingredient, StockAdapter.StockViewHolder>(StockDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        return StockViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class StockViewHolder(private val binding: RecyclerItemStockBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(ingredient: Ingredient){
            binding.ingredient = ingredient
        }
        companion object{
            fun from(parent: ViewGroup): StockViewHolder{
                val inflater = LayoutInflater.from(parent.context)
                return StockViewHolder(RecyclerItemStockBinding.inflate(inflater, parent, false))
            }
        }
    }

    class StockDiffUtil: DiffUtil.ItemCallback<Ingredient>() {
        override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
            return oldItem.name == newItem.name && oldItem.portions == newItem.portions
        }
    }
}