package com.example.vesputichallengeapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vesputichallengeapp.domain.Location

@Entity
data class LocationEntity constructor(
    @PrimaryKey
    val id: String,
    val icon: String?,
    val title: String?,
    val title_en: String?,
    val subtitle: String?,
    val subtitle_en: String?,
    val description: String?,
    val description_en: String?,
    val lng: String?,
    val lat: String?,
    val created_at: String?,
    val updated_at: String?,
    val type: String?
)

fun List<LocationEntity>.asDomainModel(): List<Location>{
    return map {
        val location = arrayOf(it.lat?.toDouble(), it.lng?.toDouble())
            Location(it.id,
            it.icon,
            it.title,
            it.title_en,
            it.subtitle,
            it.subtitle_en,
            it.description,
            it.description_en,
            location,
            it.created_at,
            it.updated_at,
            it.type)
        }
}



