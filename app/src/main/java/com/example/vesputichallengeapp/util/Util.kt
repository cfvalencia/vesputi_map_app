package com.example.vesputichallengeapp.util

import android.util.Log
import com.example.vesputichallengeapp.database.LocationEntity
import com.example.vesputichallengeapp.domain.Location
import com.google.gson.JsonObject
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point

var featureList : MutableList<Feature> = mutableListOf()

fun locationsToFeatures(locations: List<LocationEntity>): List<Feature> {


    for (location in locations){
        val lng = location.lng?.toDouble()
        val lat = location.lat?.toDouble()
        val id = location.id

        val feature = Feature.fromGeometry(
            lat?.let { it1 -> lng?.let { it2 -> Point.fromLngLat(it2, it1) } })
        feature.addStringProperty("id",id)
        feature.addStringProperty("icon",location.icon)
        feature.addStringProperty("title",location.title)
        feature.addStringProperty("title_en",location.title_en)
        feature.addStringProperty("subtitle",location.subtitle)
        feature.addStringProperty("subtitle_en",location.subtitle_en)
        feature.addStringProperty("description",location.description)
        feature.addStringProperty("description_en",location.description)
        feature.addStringProperty("created_at",location.created_at)
        feature.addStringProperty("updated_at",location.updated_at)


        featureList.add(feature)
    }

    Log.e("TEST CONVERTER", featureList.toString())
    return featureList

}

fun featurePropertiesToLocation(featureProperties: JsonObject): Location{
    val location = Location(
        featureProperties.get("id").toString(),
        featureProperties.get("icon").toString(),
        featureProperties.get("title").toString(),
        featureProperties.get("title_en").toString(),
        featureProperties.get("subtitle").toString(),
        featureProperties.get("subtitle_en").toString(),
        featureProperties.get("description").toString(),
        featureProperties.get("description_en").toString(),
        arrayOf(),
        featureProperties.get("created_at").toString(),
        featureProperties.get("updated_at").toString(),
        "SimplePoi"
    )
    return location
}

