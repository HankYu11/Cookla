package com.flowerish.cookla.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.flowerish.cookla.R
import com.flowerish.cookla.databinding.PagerItemMenuBinding
import com.flowerish.cookla.viewModels.MenuViewModel

class MenuCalendarView: Fragment() {

    private val viewModel: MenuViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<PagerItemMenuBinding>(inflater, R.layout.pager_item_menu, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

}