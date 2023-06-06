package com.devdcrq.usastateparks.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.devdcrq.usastateparks.adapter.ParksAdapter
import com.devdcrq.usastateparks.databinding.FragmentParksBinding
import com.devdcrq.usastateparks.util.Constants
import com.devdcrq.usastateparks.util.Constants.SEARCH_TIME_DELAY
import com.devdcrq.usastateparks.util.Resource
import com.devdcrq.usastateparks.viewmodel.ParksViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ParksFragment : Fragment() {

    private var _binding: FragmentParksBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ParksViewModel
    private lateinit var parksAdapter: ParksAdapter

    private var checkedItemIndex = Constants.getRandomIndex()
    private var selectedState = Constants.stateCodes[checkedItemIndex]

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentParksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()
        viewModel.getAllParks(selectedState)
        parksAdapter.setOnItemClickListener {
            val action = ParksFragmentDirections.actionParksFragmentToWebsiteFragment(it)
            findNavController().navigate(action)
            if (activity is MainActivity) (activity as MainActivity?)!!.hideBottomNavigationView()
        }
        binding.fab.setOnClickListener {
            showFilterDialog()
        }
        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchParks(editable.toString())
                    } else {
                        viewModel.getAllParks(selectedState)
                    }
                }
            }
        }
        viewModel.allParksLD.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        parksAdapter.differ.submitList(it.data.toList())
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                is Resource.Loading -> showProgressBar()
            }
        })
        viewModel.searchParksLD.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        parksAdapter.differ.submitList(it.data.toList())
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                is Resource.Loading -> showProgressBar()
            }
        })
    }

    private fun showFilterDialog() {
        val statesList: ArrayList<String> = arrayListOf()
//        statesList.add("ALL STATES")
        for (i in Constants.stateNames.indices) {
            statesList.add("${Constants.stateNames[i]}, ${Constants.stateCodes[i]}")
        }
        val a = arrayOf<String>()
        val builder = MaterialAlertDialogBuilder(requireActivity())
        builder.setTitle("Select US State:")
            .setSingleChoiceItems(statesList.toArray(a), checkedItemIndex) { dialogInterface, i ->
                selectedState = if (i == 0) ""
                else statesList[i].takeLast(2)
                checkedItemIndex = i
                viewModel.getAllParks(selectedState)
                dialogInterface.dismiss()
            }
            .setNeutralButton("Cancel") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
        builder.create().show()
    }

    override fun onResume() {
        super.onResume()
        if (activity is MainActivity) (activity as MainActivity?)!!.showBottomNavigationView()
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun setupRecyclerView() {
        parksAdapter = ParksAdapter()
        binding.rvParks.apply {
            adapter = parksAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}