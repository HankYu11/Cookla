package com.flowerish.cookla.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.flowerish.cookla.R
import com.flowerish.cookla.data.network.MarketFilter
import com.flowerish.cookla.databinding.FragmentPriceBinding
import com.flowerish.cookla.ui.adapters.PriceAdapter
import com.flowerish.cookla.viewModels.PriceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PriceFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val viewModel: PriceViewModel by viewModels()
    private val adapter = PriceAdapter()
    private lateinit var binding: FragmentPriceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_price, container, false
        )
        binding.lifecycleOwner = this
        binding.rvFridge.adapter = adapter
        binding.viewModel = viewModel

        viewModel.agricultureList.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launch {
                adapter.submitData(it)
            }
        }

        viewModel.searchCondition.observe(viewLifecycleOwner) {
            viewModel.search(it.first, it.second)
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.market,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerMarket.adapter = adapter
            }
        binding.spinnerMarket.onItemSelectedListener = this

//        return binding.root
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                PriceScreen(viewModel)
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            0 -> viewModel.setMarketFilter(MarketFilter.ALL)
            1 -> viewModel.setMarketFilter(MarketFilter.TAIPEI_MARKET)
            else -> viewModel.setMarketFilter(MarketFilter.TAIPEI_TWO)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        println("nothing Selected !!!")
    }
}

@Composable
fun PriceScreen(viewModel: PriceViewModel) {
}
