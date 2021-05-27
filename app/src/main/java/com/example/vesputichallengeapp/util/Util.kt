package com.example.vesputichallengeapp.util

import com.example.vesputichallengeapp.database.LineFeatureEntity
import com.example.vesputichallengeapp.database.LocationEntity
import com.example.vesputichallengeapp.domain.Location
import com.google.gson.JsonObject
import com.mapbox.geojson.Feature
import com.mapbox.geojson.LineString
import com.mapbox.geojson.MultiPoint
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

    return featureList

}

fun featurePropertiesToLocation(featureProperties: JsonObject): Location{
    return Location(
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
}

fun featurePointsToStringValue(points: Array<Array<Double>>): String{
    var returnval = ""
    points.iterator().forEach { value ->
        value.iterator().forEach { point ->
            returnval += point.toString()+","
        }
    }
    return returnval
}

fun listOfSegmentsToFeaturesArray(segments: List<LineFeatureEntity>): List<Feature>{
    val featureList = mutableListOf<Feature>()
    segments.listIterator().forEach { segment ->
        //create feature
        val pointlist = mutableListOf<Point>()
        //create geomtry
        val pointArray = segment.points?.split(",")?.toTypedArray()
        for (i in 0 until pointArray?.size?.minus(1)!! step 2) {
            //create the points to add to geometry
            pointlist +=createPointFromlnglat(pointArray[i].toDouble(),pointArray[i.plus(1)].toDouble())
        }
        val geometry = LineString.fromLngLats(pointlist)
        featureList += Feature.fromGeometry(geometry)
    }
    return featureList
}

fun createPointFromlnglat(lng: Double, lat: Double): Point{
    return Point.fromLngLat(lng,lat)
}



