package com.devdcrq.usastateparks.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.devdcrq.usastateparks.database.Converters
import com.devdcrq.usastateparks.databinding.FragmentWebsiteBinding
import com.devdcrq.usastateparks.model.Park
import com.devdcrq.usastateparks.model.SimplePark
import com.devdcrq.usastateparks.util.Equatable
import com.devdcrq.usastateparks.viewmodel.ParksViewModel
import com.google.android.material.snackbar.Snackbar

class WebsiteFragment : Fragment() {

    private var _binding: FragmentWebsiteBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ParksViewModel

    private val args: WebsiteFragmentArgs by navArgs()
    private lateinit var park: Equatable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebsiteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        park = args.anyPark
        binding.apply {
            webView.webViewClient = WebViewClient()
            if (park is Park) webView.loadUrl((park as Park).url)
            if (park is SimplePark) webView.loadUrl((park as SimplePark).url)
            fab.setOnClickListener {
                if (park is Park) park = Converters.convertParkToSimplePark(park as Park)
                else park as SimplePark
                if ((park as SimplePark).isFavorite) {
                    Snackbar.make(view, "Park already saved to favorites", Snackbar.LENGTH_SHORT)
                        .show()
                } else {
                    (park as SimplePark).isFavorite = true
                    viewModel.saveParkToFavorites(park as SimplePark)
                    Snackbar.make(view, "Park saved successfully", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}