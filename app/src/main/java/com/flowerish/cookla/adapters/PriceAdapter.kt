package com.flowerish.cookla.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.flowerish.cookla.R
import com.flowerish.cookla.databinding.RecyclerItemPriceBinding
import com.flowerish.cookla.databinding.RecyclerItemPriceHeaderBinding
import com.flowerish.cookla.domain.Agriculture
import com.flowerish.cookla.viewModels.PriceUiModel

class PriceAdapter : PagingDataAdapter<PriceUiModel, RecyclerView.ViewHolder>(FridgeDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            R.layout.recycler_item_price -> PriceViewHolder.from(parent)
            else -> HeaderViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val uiModel = getItem(position)
        if(uiModel is PriceUiModel.AgricultureItem) (holder as PriceViewHolder).bind(uiModel.agriculture)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is PriceUiModel.HeaderItem -> R.layout.recycler_item_price_header
            is PriceUiModel.AgricultureItem -> R.layout.recycler_item_price
            else -> throw UnsupportedOperationException("Unknown view")
        }
    }

    class PriceViewHolder(private val binding: RecyclerItemPriceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Agriculture?) {
            binding.tvName.text = item?.name?.substringBefore('-')
            binding.tvSubName.text = item?.name?.substringAfter('-')
            binding.tvPrice.text = item?.avgPrice
        }

        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return PriceViewHolder(RecyclerItemPriceBinding.inflate(inflater, parent, false))
            }
        }
    }

    class HeaderViewHolder(private val binding: RecyclerItemPriceHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return HeaderViewHolder(
                    RecyclerItemPriceHeaderBinding.inflate(
                        inflater,
                        parent,
                        false
                    )
                )
            }
        }
    }
}

class FridgeDiffUtil : DiffUtil.ItemCallback<PriceUiModel>() {
    override fun areItemsTheSame(
        oldItem: PriceUiModel,
        newItem: PriceUiModel
    ): Boolean {
        if(oldItem is PriceUiModel.HeaderItem && newItem is PriceUiModel.HeaderItem) return true
        return (oldItem is PriceUiModel.AgricultureItem && newItem is PriceUiModel.AgricultureItem
                && oldItem.agriculture.name == newItem.agriculture.name && oldItem.agriculture.avgPrice == newItem.agriculture.avgPrice)
    }

    override fun areContentsTheSame(
        oldItem: PriceUiModel,
        newItem: PriceUiModel
    ): Boolean {
        return oldItem == newItem
    }

}