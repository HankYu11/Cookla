package com.flowerish.cookla.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.flowerish.cookla.R
import com.flowerish.cookla.databinding.FragmentPriceBinding
import com.flowerish.cookla.adapters.PriceAdapter
import com.flowerish.cookla.network.MarketFilter
import com.flowerish.cookla.viewModels.PriceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PriceFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val viewModel: PriceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentPriceBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_price, container, false
        )
        binding.lifecycleOwner = this
        binding.rvFridge.adapter = PriceAdapter()
        binding.viewModel = viewModel

        viewModel.combinedData.observe(viewLifecycleOwner){
            viewModel.refreshAgricultureList(it.first, it.second)
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.market,
            android.R.layout.simple_spinner_item
        )
            .also {adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerMarket.adapter = adapter
            }
        binding.spinnerMarket.onItemSelectedListener = this
        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(position){
            0 -> viewModel.setMarketFilter(MarketFilter.ALL)
            1 -> viewModel.setMarketFilter(MarketFilter.TAIPEI_MARKET)
            else -> viewModel.setMarketFilter(MarketFilter.TAIPEI_TWO)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        println("nothing Selected !!!")
    }
}