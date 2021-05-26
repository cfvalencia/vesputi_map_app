package com.example.vesputichallengeapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.vesputichallengeapp.database.getDatabase
import com.example.vesputichallengeapp.domain.Location
import com.example.vesputichallengeapp.repository.LocationsRepository
import com.example.vesputichallengeapp.util.featurePropertiesToLocation
import com.google.gson.JsonObject
import kotlinx.coroutines.launch

class MapViewModel(application: Application): AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val locationsRepository = LocationsRepository(database)

    private var _clikedOnMarker = MutableLiveData<Location>()

    val clickedOnMarker: LiveData<Location>
        get() = _clikedOnMarker

    val locationsList = locationsRepository.locations

    val lineFeatureList = locationsRepository.lineFeatures


    init {
        Log.e("TEST: ","GET INFO")
        getLocationsFromServer()
    }

    private fun getLocationsFromServer() {
        viewModelScope.launch {
            try {
                //locationsRepository.getLocations()
                Log.e("TEST: ","TRY")
                locationsRepository.getLineFeatures()
            } catch (e: Exception) {
                Log.e("ERROR: ",e.toString())
            }
        }
    }

    fun getLocationWithId(feature: JsonObject) {
        _clikedOnMarker.value = featurePropertiesToLocation(feature)
    }


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MapViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }


}