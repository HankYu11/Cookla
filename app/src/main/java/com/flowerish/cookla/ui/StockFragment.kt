package com.flowerish.cookla.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.flowerish.cookla.R
import com.flowerish.cookla.adapters.StockAdapter
import com.flowerish.cookla.databinding.FragmentStockBinding
import com.flowerish.cookla.databinding.LayoutAddStockPopupBinding
import com.flowerish.cookla.viewModels.StockViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class StockFragment : Fragment() {

    private val viewModel: StockViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentStockBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_stock, container, false)
        binding.lifecycleOwner = this
        binding.rvStock.adapter = StockAdapter()
        binding.viewModel = viewModel

        viewModel.popupAdd.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let {
                AddWindow(viewModel).show(parentFragmentManager, "stock")
            }
        }

        return binding.root
    }

    class AddWindow(val viewModel: StockViewModel) :
        DialogFragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            val binding = DataBindingUtil.inflate<LayoutAddStockPopupBinding>(
                inflater,
                R.layout.layout_add_stock_popup,
                container,
                false
            )
            binding.btnAddIngredient.setOnClickListener {
                viewModel.addIngredient(
                    binding.etIngredientName.text.toString(),
                    binding.etIngredientAmount.text.toString().toInt(),
                    LocalDate.now().plusDays(binding.etIngredientExpiringDate.text.toString().toLong())
                )
                this@AddWindow.dismiss()

            }
            return binding.root
        }
    }
}