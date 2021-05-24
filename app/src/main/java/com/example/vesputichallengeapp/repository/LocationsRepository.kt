package com.example.vesputichallengeapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.vesputichallengeapp.database.LocationEntity
import com.example.vesputichallengeapp.database.LocationsDatabase
import com.example.vesputichallengeapp.domain.NetworkLocationsContainer
import com.example.vesputichallengeapp.domain.asDataBaseModel
import com.example.vesputichallengeapp.network.LocationsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocationsRepository(private val database: LocationsDatabase) {

    val locations: LiveData<List<LocationEntity>> =
        database.locationDao.getLocations()

    suspend fun getLocations(){
        withContext(Dispatchers.IO){
            val locationsList = LocationsApi.retrofitService.getProperties()
            val locationsListTwo = LocationsApi.retrofitService.getPropertiesTwo()
            //Log.e("LOCATION TWO", locationsListTwo.toString())
            database.locationDao.insertAll(*NetworkLocationsContainer(locationsList).asDataBaseModel())
        }
    }

}