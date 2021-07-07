package com.flowerish.cookla.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.flowerish.cookla.R
import com.flowerish.cookla.adapters.BuyingAdapter
import com.flowerish.cookla.adapters.StockAdapter
import com.flowerish.cookla.databinding.FragmentBuyingBinding
import com.flowerish.cookla.viewModels.BuyingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BuyingFragment: Fragment() {

    private val viewModel: BuyingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentBuyingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_buying, container, false)

        binding.lifecycleOwner = this
        binding.rvBuying.adapter = BuyingAdapter()
        binding.viewModel = viewModel

        return binding.root
    }
}