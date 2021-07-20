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
import com.flowerish.cookla.databinding.FragmentMenuBinding
import com.flowerish.cookla.adapters.DayAdapter
import com.flowerish.cookla.databinding.LayoutAddMenuPopupBinding
import com.flowerish.cookla.viewModels.MenuViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.util.*

@AndroidEntryPoint
class MenuFragment : Fragment() {
    private lateinit var binding: FragmentMenuBinding

    private val viewModel: MenuViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        //todo viewpager adapter

        viewModel.popupAdd.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { date ->
                AddWindow(viewModel, date).show(parentFragmentManager, "Add")
            }
        }

        return binding.root
    }

    class AddWindow(val viewModel: MenuViewModel, val date: LocalDate) : DialogFragment() {
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
                viewModel.onPopupAddClick(
                    date,
                    binding.etIngredientName.text.toString(),
                    binding.etIngredientAmount.text.toString().toInt()
                )
                this@AddWindow.dismiss()
            }
            return binding.root
        }
    }
}