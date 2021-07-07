package com.flowerish.cookla.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flowerish.cookla.databinding.RecyclerItemPriceBinding
import com.flowerish.cookla.domain.Agriculture

class PriceAdapter : ListAdapter<Agriculture, PriceAdapter.PriceViewHolder>(FridgeDiffUtil()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceViewHolder {
        return PriceViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PriceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PriceViewHolder(private val binding: RecyclerItemPriceBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : Agriculture){
            binding.tvName.text = item.name
            binding.tvPrice.text = item.avgPrice
        }

        companion object{
            fun from(parent: ViewGroup): PriceViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return PriceViewHolder(RecyclerItemPriceBinding.inflate(inflater,parent,false))
            }
        }
    }
}

class FridgeDiffUtil : DiffUtil.ItemCallback<Agriculture>(){
    override fun areItemsTheSame(
        oldItem: Agriculture,
        newItem: Agriculture
    ): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(
        oldItem: Agriculture,
        newItem: Agriculture
    ): Boolean {
        return oldItem.avgPrice == newItem.avgPrice && oldItem.name == newItem.name
    }

}