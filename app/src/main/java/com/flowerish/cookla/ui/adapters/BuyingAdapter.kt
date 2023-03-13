package com.flowerish.cookla.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flowerish.cookla.databinding.RecyclerItemBuyingBinding
import com.flowerish.cookla.domain.BuyingIngredient

class BuyingAdapter :
    ListAdapter<BuyingIngredient, BuyingAdapter.BuyingViewHolder>(BuyingDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyingViewHolder {
        return BuyingViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: BuyingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class BuyingViewHolder(private val binding: RecyclerItemBuyingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ingredient: BuyingIngredient) {
            binding.ingredient = ingredient
        }

        companion object {
            fun from(parent: ViewGroup): BuyingViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return BuyingViewHolder(
                    RecyclerItemBuyingBinding.inflate(
                        inflater,
                        parent,
                        false
                    )
                )
            }
        }
    }

    class BuyingDiffUtil() : DiffUtil.ItemCallback<BuyingIngredient>() {
        override fun areItemsTheSame(
            oldItem: BuyingIngredient,
            newItem: BuyingIngredient
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: BuyingIngredient,
            newItem: BuyingIngredient
        ): Boolean {
            return oldItem.name == newItem.name && oldItem.portion == newItem.portion
        }

    }
}