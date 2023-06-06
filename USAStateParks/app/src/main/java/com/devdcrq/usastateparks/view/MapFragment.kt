package com.devdcrq.usastateparks.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.devdcrq.usastateparks.R
import com.devdcrq.usastateparks.database.Converters
import com.devdcrq.usastateparks.databinding.FragmentMapBinding
import com.devdcrq.usastateparks.model.SimplePark
import com.devdcrq.usastateparks.util.Constants.MAP_API_KEY
import com.devdcrq.usastateparks.util.Resource
import com.devdcrq.usastateparks.viewmodel.ParksViewModel
import com.tomtom.sdk.location.GeoPoint
import com.tomtom.sdk.map.display.MapOptions
import com.tomtom.sdk.map.display.TomTomMap
import com.tomtom.sdk.map.display.common.screen.Padding
import com.tomtom.sdk.map.display.image.ImageFactory
import com.tomtom.sdk.map.display.marker.Marker
import com.tomtom.sdk.map.display.marker.MarkerOptions
import com.tomtom.sdk.map.display.ui.MapFragment
import com.tomtom.sdk.map.display.ui.MapReadyCallback

class MapFragment : Fragment(), MapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var tomTomMap: TomTomMap
    lateinit var viewModel: ParksViewModel
    private var parkList = mutableListOf<SimplePark>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val mapOptions = MapOptions(mapKey = MAP_API_KEY, padding = Padding(50))
        val mapFragment = MapFragment.newInstance(mapOptions)
        parentFragmentManager.beginTransaction()
            .replace(R.id.map_container, mapFragment)
            .commit()
        mapFragment.getMapAsync(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        viewModel.allParksLD.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        it.data.forEach { park ->
                            parkList.add(Converters.convertParkToSimplePark(park))
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    parkList = mutableListOf()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                is Resource.Loading -> showProgressBar()
            }
        })
        viewModel.getFavoriteParks().observe(viewLifecycleOwner, Observer { parks ->
            val ids = parks.map { it.id }
            parkList.filter { it.id in ids }.forEach { it.isFavorite = true }
        })
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

    override fun onMapReady(map: TomTomMap) {
        tomTomMap = map
        for (park in parkList) {
            if (park.latitude.isNotEmpty() && park.longitude.isNotEmpty()) {
                val position = GeoPoint(park.latitude.toDouble(), park.longitude.toDouble())
                val pinFavPark = ImageFactory.fromResource(R.drawable.ic_favorite)
                val pinOtherPark = ImageFactory.fromResource(R.drawable.ic_pin)
                val markerOptions = MarkerOptions(
                    coordinate = position,
                    pinImage = if (park.isFavorite) pinFavPark else pinOtherPark,
                    balloonText = park.name
                )
                tomTomMap.zoomToMarkers()
                tomTomMap.addMarker(markerOptions)
                tomTomMap.addMarkerClickListener { marker: Marker ->
                    if (!marker.isSelected()) {
                        marker.select()
                    }
                }
            } else continue

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}