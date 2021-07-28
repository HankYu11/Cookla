package com.flowerish.cookla.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flowerish.cookla.databinding.PagerItemMenuBinding
import com.flowerish.cookla.domain.DayWithIngredients
import com.flowerish.cookla.viewModels.MenuViewModel
import java.util.*

class MenuPagerAdapter(private val viewModel: MenuViewModel): ListAdapter<List<DayWithIngredients>, MenuPagerAdapter.MenuViewHolder>(MenuDiffCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel)
    }

    class MenuViewHolder(val binding: PagerItemMenuBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(weekList: List<DayWithIngredients>, viewModel: MenuViewModel){
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
                return oldItem == newItem
            }
        }
    }
}