package com.example.vesputichallengeapp.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LocationDao{
    @Query("select * from locationentity where type=\"SimplePoi\"")
    fun getLocations(): LiveData<List<LocationEntity>>

    @Query("select * from linefeatureentity ")
    fun getLineFeatures(): LiveData<List<LineFeatureEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg locations: LocationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllFeatures(vararg features: LineFeatureEntity)
}

@Database(entities = [LocationEntity::class,LineFeatureEntity::class],version = 4)
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