package com.devdcrq.usastateparks.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devdcrq.usastateparks.adapter.ParksAdapter
import com.devdcrq.usastateparks.databinding.FragmentFavoritesBinding
import com.devdcrq.usastateparks.model.SimplePark
import com.devdcrq.usastateparks.viewmodel.ParksViewModel
import com.google.android.material.snackbar.Snackbar

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: ParksViewModel
    lateinit var parksAdapter: ParksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()
        parksAdapter.setOnItemClickListener {
            val action = FavoritesFragmentDirections.actionFavoritesFragmentToWebsiteFragment(it)
            findNavController().navigate(action)
            if (activity is MainActivity) (activity as MainActivity?)!!.hideBottomNavigationView()
        }
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val park = parksAdapter.differ.currentList[position] as SimplePark
                viewModel.deleteParkFromFavorites(park)
                Snackbar.make(view, "Successfully deleted park", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.saveParkToFavorites(park)
                    }
                }.show()
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvFavoriteParks)
        }

        viewModel.getFavoriteParks().observe(viewLifecycleOwner, Observer { parks ->
            parksAdapter.differ.submitList(parks)
        })
    }

    override fun onResume() {
        super.onResume()
        if (activity is MainActivity) (activity as MainActivity?)!!.showBottomNavigationView()
    }

    private fun setupRecyclerView() {
        parksAdapter = ParksAdapter()
        binding.rvFavoriteParks.apply {
            adapter = parksAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}