package com.devdcrq.usastateparks.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devdcrq.usastateparks.application.ParksApplication
import com.devdcrq.usastateparks.model.ParksResponse
import com.devdcrq.usastateparks.model.SimplePark
import com.devdcrq.usastateparks.repository.ParksRepository
import com.devdcrq.usastateparks.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class ParksViewModel(app: Application, private val parksRepository: ParksRepository) :
    AndroidViewModel(app) {

    val allParksLD: MutableLiveData<Resource<ParksResponse>> = MutableLiveData()
    val searchParksLD: MutableLiveData<Resource<ParksResponse>> = MutableLiveData()

    fun getAllParks(stateCode: String) = viewModelScope.launch {
        safeCall(false, stateCode)
    }

    fun searchParks(searchQuery: String) = viewModelScope.launch {
        safeCall(true, "", searchQuery)
    }

    private suspend fun safeCall(isSearching: Boolean, stateCode: String, searchQuery: String? = null) {
        if (!isSearching) allParksLD.postValue(Resource.Loading())
        else searchParksLD.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                if (!isSearching) {
                    val response = parksRepository.getAllParks(stateCode)
                    allParksLD.postValue(handleAllParksResponse(response))
                } else {
                    val response = parksRepository.searchParks(searchQuery!!)
                    searchParksLD.postValue(handleAllParksResponse(response))
                }
            } else {
                if (!isSearching) allParksLD.postValue(Resource.Error("No internet connection"))
                else searchParksLD.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    if (!isSearching) allParksLD.postValue(Resource.Error("Network failure"))
                    else searchParksLD.postValue(Resource.Error("Network failure"))
                }
                else -> {
                    if (!isSearching) allParksLD.postValue(Resource.Error("Conversion error"))
                    else searchParksLD.postValue(Resource.Error("Conversion error"))
                }
            }
        }
    }

    private fun handleAllParksResponse(response: Response<ParksResponse>): Resource<ParksResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<ParksApplication>()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    fun saveParkToFavorites(simplePark: SimplePark) = viewModelScope.launch {
        parksRepository.upsertFavoritePark(simplePark)
    }

    fun getFavoriteParks() = parksRepository.getAllFavoriteParks()

    fun deleteParkFromFavorites(simplePark: SimplePark) = viewModelScope.launch {
        parksRepository.deleteFavoritePark(simplePark)
    }

}