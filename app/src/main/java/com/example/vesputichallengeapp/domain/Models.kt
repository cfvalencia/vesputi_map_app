package com.example.vesputichallengeapp.domain

import android.os.Parcelable
import com.example.vesputichallengeapp.database.LineFeatureEntity
import com.example.vesputichallengeapp.database.LocationEntity
import com.example.vesputichallengeapp.util.featurePointsToStringValue
import kotlinx.android.parcel.Parcelize


data class NetworkLocationsContainer(val locations: List<Location>)

data class NetworkLIneFeatureContainer(val features: List<LineFeature>)


@Parcelize
data class Location(
    val id: String,
    val icon: String?,
    val title: String?,
    val title_en: String?,
    val subtitle: String?,
    val subtitle_en: String?,
    val description: String?,
    val description_en: String?,
    val position: Array<Double?>?,
    val created_at: String?,
    val updated_at: String?,
    val type: String?): Parcelable

@Parcelize
data class LineFeature(
    val id: String,
    val points: Array<Array<Double>>,
    val properties: Properties?): Parcelable

@Parcelize
data class Properties(
    val routes: Route?
):Parcelable

@Parcelize
data class Route(
    val day: Array<Double>
):Parcelable

fun NetworkLocationsContainer.asDataBaseModel(): Array<LocationEntity>{
    return locations.map {
        LocationEntity(
            it.id,
            it.icon,
            it.title,
            it.title_en,
            it.subtitle,
            it.subtitle_en,
            it.description,
            it.description_en,
            it.position?.get(0)?.toString(),
            it.position?.get(1)?.toString(),
            it.created_at,
            it.updated_at,
            it.type
        )
    }.toTypedArray()
}

fun NetworkLIneFeatureContainer.asDataBaseModel(): Array<LineFeatureEntity>{
    return features.map { feature ->
        LineFeatureEntity(
            feature.id,
            featurePointsToStringValue(feature.points),
            feature.properties?.routes?.toString()
        )
    }.toTypedArray()
}
