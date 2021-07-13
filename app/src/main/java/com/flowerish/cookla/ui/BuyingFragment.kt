package com.flowerish.cookla.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.flowerish.cookla.R
import com.flowerish.cookla.adapters.BuyingAdapter
import com.flowerish.cookla.databinding.FragmentBuyingBinding
import com.flowerish.cookla.databinding.LayoutAddMenuPopupBinding
import com.flowerish.cookla.databinding.LayoutAddStockPopupBinding
import com.flowerish.cookla.observeInLifecycle
import com.flowerish.cookla.viewModels.BuyingEvent
import com.flowerish.cookla.viewModels.BuyingViewModel
import com.flowerish.cookla.viewModels.StockViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate

@AndroidEntryPoint
class BuyingFragment : Fragment() {

    private val viewModel: BuyingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentBuyingBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_buying, container, false)

        binding.lifecycleOwner = this
        binding.rvBuying.adapter = BuyingAdapter()
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.eventsFlow
            .onEach {
                when(it){
                    BuyingEvent.Add -> AddWindow(viewModel).show(parentFragmentManager, "buying")
                }
            }.observeInLifecycle(this)
    }

    class AddWindow(val viewModel: BuyingViewModel) : DialogFragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            val binding = DataBindingUtil.inflate<LayoutAddMenuPopupBinding>(
                inflater,
                R.layout.layout_add_menu_popup,
                container,
                false
            )
            binding.btnAddIngredient.setOnClickListener {
                viewModel.addBuyingIngredient(
                    binding.etIngredientName.text.toString(),
                    binding.etIngredientAmount.text.toString().toInt()
                )
                this@AddWindow.dismiss()
            }
            return binding.root
        }
    }
}