package com.example.fridge.price

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fridge.databinding.RecyclerItemPriceBinding

class FridgeRVAdp : ListAdapter<AgricultureProperty, FridgeRVAdp.FridgeViewHolder>(FridgeDiffUtil()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FridgeViewHolder {
        println("ViewHolder !!!")
        return FridgeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FridgeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FridgeViewHolder(private val binding: RecyclerItemPriceBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : AgricultureProperty){
            binding.tvName.text = item.name
            binding.tvPrice.text = item.avgPrice
        }

        companion object{
            fun from(parent: ViewGroup): FridgeViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return FridgeViewHolder(RecyclerItemPriceBinding.inflate(inflater,parent,false))
            }
        }
    }
}

class FridgeDiffUtil : DiffUtil.ItemCallback<AgricultureProperty>(){
    override fun areItemsTheSame(
        oldItem: AgricultureProperty,
        newItem: AgricultureProperty
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: AgricultureProperty,
        newItem: AgricultureProperty
    ): Boolean {
        return oldItem.avgPrice == newItem.avgPrice
    }

}