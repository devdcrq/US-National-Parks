package com.devdcrq.usastateparks.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.devdcrq.usastateparks.R
import com.devdcrq.usastateparks.database.ParksDatabase
import com.devdcrq.usastateparks.databinding.ActivityMainBinding
import com.devdcrq.usastateparks.repository.ParksRepository
import com.devdcrq.usastateparks.viewmodel.ParksViewModel
import com.devdcrq.usastateparks.viewmodel.ParksViewModelProviderFactory

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: ParksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavView.setupWithNavController(navController)

        val parksRepository = ParksRepository(ParksDatabase(this))
        val viewModelProviderFactory = ParksViewModelProviderFactory(application, parksRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[ParksViewModel::class.java]
    }

    fun hideBottomNavigationView() {
        binding.bottomNavView.apply {
            clearAnimation()
            animate().translationY(height.toFloat()).duration = 300
            visibility = View.GONE
        }
    }

    fun showBottomNavigationView() {
        binding.bottomNavView.apply {
            clearAnimation()
            animate().translationY(0f).duration = 300
            visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}