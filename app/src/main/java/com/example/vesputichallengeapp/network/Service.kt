package com.example.vesputichallengeapp.network

import com.example.vesputichallengeapp.domain.Location
import com.example.vesputichallengeapp.domain.LocationTwo
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
private const val BASE_URL = "https://insa-ms.netzmap.com/"

//private const val BASE_URL = "https://midgard.netzmap.com/"

// Build Moshi objet for retrofit adding kotlin adapter for compatibility
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// Retrofit builder using Moshi converter
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

// Interface to expose "getLocations" method
interface LocationsApiService{

    @GET("stations.json")
    suspend fun getPropertiesTwo(): List<LocationTwo>

    @GET("features.json?app_mode=swh-mein-halle-mobil")
    suspend fun getProperties(): List<Location>
}

object LocationsApi{
    val retrofitService: LocationsApiService by lazy { retrofit.create(LocationsApiService::class.java) }
}