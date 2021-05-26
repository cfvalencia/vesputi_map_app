package com.example.vesputichallengeapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.vesputichallengeapp.database.LineFeatureEntity
import com.example.vesputichallengeapp.database.LocationEntity
import com.example.vesputichallengeapp.database.LocationsDatabase
import com.example.vesputichallengeapp.domain.NetworkLIneFeatureContainer
import com.example.vesputichallengeapp.domain.NetworkLocationsContainer
import com.example.vesputichallengeapp.domain.asDataBaseModel
import com.example.vesputichallengeapp.network.LocationsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocationsRepository(private val database: LocationsDatabase) {

    val locations: LiveData<List<LocationEntity>> =
        database.locationDao.getLocations()

    val lineFeatures: LiveData<List<LineFeatureEntity>> =
        database.locationDao.getLineFeatures()

    suspend fun getLocations(){
        withContext(Dispatchers.IO){
            val locationsList = LocationsApi.retrofitService.getProperties()
            database.locationDao.insertAll(*NetworkLocationsContainer(locationsList).asDataBaseModel())
        }
    }

    suspend fun getLineFeatures(){
        Log.e("TEST: ","SUSPEND")
        withContext(Dispatchers.IO){
            val lineFeatureList = LocationsApi.retrofitService.getLineFeatures()
            Log.e("LIST FEATURES",lineFeatureList.toString())
            //val locationsList = LocationsApi.retrofitService.getProperties()
            database.locationDao.insertAllFeatures(*NetworkLIneFeatureContainer(lineFeatureList).asDataBaseModel())
        }
    }

}