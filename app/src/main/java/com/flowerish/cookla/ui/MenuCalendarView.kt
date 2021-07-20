package com.flowerish.cookla.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.flowerish.cookla.R
import com.flowerish.cookla.databinding.PagerItemMenuBinding

class MenuCalendarView: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<PagerItemMenuBinding>(inflater, R.layout.pager_item_menu, container, false)


        return binding.root
    }

    private fun previousWeek(){
        
    }
}