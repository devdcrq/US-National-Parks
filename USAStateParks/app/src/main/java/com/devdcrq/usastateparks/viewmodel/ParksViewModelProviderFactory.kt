package com.devdcrq.usastateparks.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.devdcrq.usastateparks.repository.ParksRepository

class ParksViewModelProviderFactory(val app: Application, private val parksRepository: ParksRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ParksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ParksViewModel(app, parksRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
