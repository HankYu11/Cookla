package com.example.fridge.pager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fridge.price.PriceFragment
import com.example.fridge.R
import com.example.fridge.recipe.RecipeFragment
import com.example.fridge.stock.StockFragment
import com.example.fridge.databinding.FragmentPagerBinding
import com.example.fridge.menu.MenuFragment
import com.google.android.material.tabs.TabLayoutMediator

class PagerFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentPagerBinding>(inflater,R.layout.fragment_pager,container,false)
        binding.viewPager.adapter = PagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        TabLayoutMediator(binding.tabLayout, binding.viewPager){tab, position ->
            when(position){
                0 -> tab.text = "Menu"
                1 -> tab.text = "Stock"
                2 -> tab.text = "Recipe"
                3 -> tab.text = "Price"
            }
        }.attach()
        return binding.root
    }
}

class PagerAdapter(val fragment : Fragment) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> MenuFragment()
            1 -> StockFragment()
            2 -> RecipeFragment()
            else -> PriceFragment()
        }
    }
}
