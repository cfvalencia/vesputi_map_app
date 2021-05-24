package com.example.vesputichallengeapp.domain

import android.os.Parcelable
import com.example.vesputichallengeapp.database.LocationEntity
import kotlinx.android.parcel.Parcelize


data class NetworkLocationsContainer(val locations: List<Location>)
data class NetworkLocationsTwoContainer(val locations: List<LocationTwo>)

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
    val type: String?): Parcelable {
}

@Parcelize
data class LocationTwo(
    val id: String,
    val properties: Properties?,
    val title: String?): Parcelable {
}

@Parcelize
data class Properties(
    val position: Position,
    val topogram_position: TopogramPosition?,
    val name: Name?,
    val city: Name?): Parcelable {
}

@Parcelize
data class Position(
    val default: Array<String>?): Parcelable {
}

@Parcelize
data class Name(
    val default:String?): Parcelable {
}

@Parcelize
data class TopogramPosition(
    val day: Array<String>?): Parcelable {
}

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
