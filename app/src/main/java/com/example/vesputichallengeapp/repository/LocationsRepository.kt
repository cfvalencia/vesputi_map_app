package com.example.vesputichallengeapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.vesputichallengeapp.database.LocationEntity
import com.example.vesputichallengeapp.database.LocationsDatabase
import com.example.vesputichallengeapp.domain.Location
import com.example.vesputichallengeapp.domain.NetworkLocationsContainer
import com.example.vesputichallengeapp.domain.asDataBaseModel
import com.example.vesputichallengeapp.network.LocationsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocationsRepository(private val database: LocationsDatabase) {

    suspend fun getLocationFromDb(): LiveData<List<LocationEntity>>{
        val list: LiveData<List<LocationEntity>>
        withContext(Dispatchers.IO){
            list = database.locationDao.getLocations()
        }
        return list
    }

    val locations: LiveData<List<LocationEntity>> =
        database.locationDao.getLocations()


    lateinit var location: Location

    suspend fun getLocationWithId(id:String) : Location{
        withContext(Dispatchers.IO){
            val entity = database.locationDao.getLocationWithId()
            val locationFromArray = arrayOf(entity.value?.lat?.toDouble(), entity.value?.lng?.toDouble())
            location = Location(
                entity.value!!.id,
                entity.value!!.icon,
                entity.value!!.title,
                entity.value!!.title_en,
                entity.value!!.subtitle,
                entity.value!!.subtitle_en,
                entity.value!!.description,
                entity.value!!.description_en,
                locationFromArray,
                entity.value!!.created_at,
                entity.value!!.updated_at,
                entity.value!!.type)
        }
       return location
    }


    suspend fun getLocations(){
        withContext(Dispatchers.IO){
            val locationsList = LocationsApi.retrofitService.getProperties()
            database.locationDao.insertAll(*NetworkLocationsContainer(locationsList).asDataBaseModel())
        }
    }



}