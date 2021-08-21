package com.flowerish.cookla.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.flowerish.cookla.R
import com.flowerish.cookla.adapters.MenuPagerAdapter
import com.flowerish.cookla.databinding.FragmentMenuBinding
import com.flowerish.cookla.databinding.LayoutAddMenuPopupBinding
import com.flowerish.cookla.observeInLifecycle
import com.flowerish.cookla.viewModels.MenuViewModel
import com.flowerish.cookla.viewModels.MenuViewModel.MenuEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class MenuFragment : Fragment() {
    private lateinit var binding: FragmentMenuBinding

    private val viewModel: MenuViewModel by viewModels()
    private lateinit var adapter: MenuPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu, container, false)
        setupBinding()
        setupObserve()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.eventsFlow
            .onEach {
                when (it) {
                    is MenuEvent.RefreshData -> {
                        binding.menuViewPager.adapter?.notifyItemChanged(binding.menuViewPager.currentItem)
                    }
                    is MenuEvent.ScrollToCurrentDay -> {
                        binding.menuViewPager.setCurrentItem(it.currentDayPosition, false)
                    }
                }
            }
            .observeInLifecycle(viewLifecycleOwner)
    }

    private fun setupObserve() {
        viewModel.pagerWeekList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.popupAdd.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { date ->
                MenuAddWindow(
                    date,
                    binding.menuViewPager.currentItem
                ) { mDate, ingredientName, ingredientAmount, currentPage ->
                    viewModel.onPopupAddClick(mDate, ingredientName, ingredientAmount, currentPage)
                }
                    .show(parentFragmentManager, "Add")
            }
        }
    }

    private fun setupBinding() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        adapter = MenuPagerAdapter(viewModel) {weekList, position ->
            viewModel.updateDayWithIngredients(weekList, position)
        }
        binding.menuViewPager.adapter = adapter
        binding.menuViewPager.offscreenPageLimit = 1
    }

}

class MenuAddWindow(
    private val date: LocalDate, private val currentPage: Int,
    private val submitClick: (date: LocalDate, ingredientName: String, ingredientAmount: Int, currentPage: Int) -> Unit
) : DialogFragment() {
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
            submitClick(
                date,
                binding.etIngredientName.text.toString(),
                binding.etIngredientAmount.text.toString().toInt(),
                currentPage
            )
            this.dismiss()
        }
        return binding.root
    }
}