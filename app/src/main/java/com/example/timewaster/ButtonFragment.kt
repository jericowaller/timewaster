package com.example.timewaster

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timewaster.data.DB
import com.example.timewaster.databinding.FragmentButtonBinding

class ButtonFragment : Fragment() {

    private val viewModel: TWViewModel by activityViewModels {
        TWViewModelFactory(
            (activity?.application as TimewasterApplication).database
                .DBDao()
        )
    }

    lateinit var DB: DB


    private var _binding: FragmentButtonBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentButtonBinding.inflate(inflater, container, false)
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
            text.text = DB.points.toString()
            button.setOnClickListener{viewModel.addPoints(DB, 1)}
            button2.setOnClickListener{ findNavController().navigate(R.id.action_buttonFragment_to_shopFragment)}
        }

    }


}