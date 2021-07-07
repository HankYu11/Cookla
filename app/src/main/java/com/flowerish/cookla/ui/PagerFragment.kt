package com.flowerish.cookla.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.flowerish.cookla.R
import com.flowerish.cookla.databinding.FragmentPagerBinding
import com.google.android.material.tabs.TabLayoutMediator

class PagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentPagerBinding>(inflater,R.layout.fragment_pager,container,false)
        binding.viewPager.adapter = PagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        TabLayoutMediator(binding.tabLayout, binding.viewPager){tab, position ->
            when(position){
                0 -> {
                    tab.text = "Recipe"
                    tab.icon = ContextCompat.getDrawable(requireContext(),R.drawable.ic_recipe)
                }
                1 -> {
                    tab.text = "Storage"
                    tab.icon = ContextCompat.getDrawable(requireContext(),R.drawable.ic_storage)
                }
                2 -> {
                    tab.text = "Menu"
                    tab.icon = ContextCompat.getDrawable(requireContext(),R.drawable.ic_menu)
                }
                3 -> {
                    tab.text = "List"
                    tab.icon = ContextCompat.getDrawable(requireContext(),R.drawable.ic_buying_list)
                }
                4 -> {
                    tab.text = "Price"
                    tab.icon = ContextCompat.getDrawable(requireContext(),R.drawable.ic_price)
                }
            }
        }.attach()

        return binding.root
    }
}

class PagerAdapter(val fragment : Fragment) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> RecipeFragment()
            1 -> StockFragment()
            2 -> MenuFragment()
            3 -> BuyingFragment()
            else -> PriceFragment()
        }
    }
}
