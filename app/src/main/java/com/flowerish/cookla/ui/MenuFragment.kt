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
import com.flowerish.cookla.adapters.MenuPagerAdapter
import com.flowerish.cookla.databinding.FragmentMenuBinding
import com.flowerish.cookla.databinding.LayoutAddMenuPopupBinding
import com.flowerish.cookla.observeInLifecycle
import com.flowerish.cookla.viewModels.MenuViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
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

        val adapter = MenuPagerAdapter(viewModel)
        binding.menuViewPager.adapter = adapter

        viewModel.pagerWeekList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.menuViewPager.setCurrentItem(viewModel.currentDayPosition, false)
        }

        viewModel.popupAdd.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { date ->
                AddWindow(viewModel, date, binding.menuViewPager.currentItem).show(parentFragmentManager, "Add")
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.eventsFlow
            .onEach {
                when(it){
                    MenuViewModel.MenuEvent.RefreshData -> {
                        Timber.d("Event !!!")
                        binding.menuViewPager.adapter?.notifyItemChanged(binding.menuViewPager.currentItem)
                    }
                }
            }
            .observeInLifecycle(viewLifecycleOwner)
    }

    class AddWindow(val viewModel: MenuViewModel, val date: LocalDate, val currentPage: Int) : DialogFragment() {
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
                    binding.etIngredientAmount.text.toString().toInt(),
                    currentPage
                )
                this@AddWindow.dismiss()
            }
            return binding.root
        }
    }
}