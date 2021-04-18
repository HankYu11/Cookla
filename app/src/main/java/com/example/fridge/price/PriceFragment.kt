package com.example.fridge.price

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fridge.R
import com.example.fridge.databinding.FragmentPriceBinding
import kotlinx.android.synthetic.main.fragment_price.*

class PriceFragment : Fragment() {

    private val viewModel : PriceViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentPriceBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_price,container,false)
        val adapter = FridgeRVAdp()
        binding.rvFridge.adapter = adapter
        binding.rvFridge.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL,false)
        viewModel.getAgricultureProperties()
        viewModel.lstAgriculture.observe(viewLifecycleOwner,  {
            adapter.submitList(it)
            for(i in 0..10){
                println("${it[i].name} !!!")
            }
        })

        val spinnerAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.market, android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMarket.adapter = spinnerAdapter

        return binding.root
    }


}