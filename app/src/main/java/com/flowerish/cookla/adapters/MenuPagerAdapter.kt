package com.flowerish.cookla.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flowerish.cookla.databinding.PagerItemMenuBinding
import java.util.*

class MenuPagerAdapter: ListAdapter<List<Calendar>, MenuPagerAdapter.MenuViewHolder>(MenuDiffCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MenuViewHolder(val binding: PagerItemMenuBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(weekList: List<Calendar>){
            binding.weekList = weekList
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
        object MenuDiffCallBack: DiffUtil.ItemCallback<List<Calendar>>() {
            override fun areItemsTheSame(
                oldItem: List<Calendar>,
                newItem: List<Calendar>
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: List<Calendar>,
                newItem: List<Calendar>
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}