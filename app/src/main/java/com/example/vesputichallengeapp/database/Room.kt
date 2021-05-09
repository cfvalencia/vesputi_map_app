package com.example.vesputichallengeapp.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface LocationDao{
    @Query("select * from locationentity where type=\"SimplePoi\"")
    fun getLocations(): LiveData<List<LocationEntity>>

    @Query("select * from locationentity where id=662")
    fun getLocationWithId(): LiveData<LocationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg locations: LocationEntity)
}

@Database(entities = [LocationEntity::class],version = 2)
abstract class LocationsDatabase: RoomDatabase(){
    abstract val locationDao: LocationDao
}

private lateinit var INSTANCE:LocationsDatabase

fun getDatabase(context: Context): LocationsDatabase{
    synchronized(LocationsDatabase::class.java){
        if(!::INSTANCE.isInitialized){
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                LocationsDatabase::class.java,
                "locations")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}