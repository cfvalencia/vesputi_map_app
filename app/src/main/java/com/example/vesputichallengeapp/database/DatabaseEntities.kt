package com.example.vesputichallengeapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

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




