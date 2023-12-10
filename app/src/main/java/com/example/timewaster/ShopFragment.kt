package com.example.timewaster

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.timewaster.data.DB
import com.example.timewaster.databinding.FragmentShopBinding

class ShopFragment : Fragment() {

    private val viewModel: TWViewModel by activityViewModels {
        TWViewModelFactory(
            (activity?.application as TimewasterApplication).database
                .DBDao()
        )
    }

    lateinit var DB: DB


    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = 1
        viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
            DB = selectedItem
            bind(DB)
        }
    }

    private fun bind(DB: DB) {

        binding.apply {
            clickerCount.text = "Current amount: " + DB.clickers.toString()
            superclickerCount.text = "Current amount: " + DB.superClickers.toString()
            pts.text = "Balance: " + DB.points.toString()
            clickerButton.setOnClickListener{viewModel.buyClicker(DB)}
            superclickerButton.setOnClickListener{viewModel.buySuperClicker(DB)}
            backButton.setOnClickListener{ findNavController().navigate(R.id.action_shopFragment_to_buttonFragment)}
            winButton.setOnClickListener{viewModel.win(DB)}
            }

        }
        }
