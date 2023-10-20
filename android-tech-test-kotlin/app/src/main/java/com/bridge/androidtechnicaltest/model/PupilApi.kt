package com.bridge.androidtechnicaltest.model

import android.os.Parcelable
import com.bridge.androidtechnicaltest.db.Pupil
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PupilDataApi(
    val items: List<PupilDetailApi>,
    val itemCount: Int,
    val pageNumber: Int,
    val totalPages: Int
) : Parcelable

@Parcelize
data class PupilDetailApi(
    val pupilId: Long,
    val country: String,
    val name: String,
    val image: String,
    val latitude: Double,
    val longitude: Double
) : Parcelable {

    fun mapToPupil(): Pupil {
        return Pupil(
            pupilId,
            name,
            country,
            image,
            latitude,
            longitude
        )
    }
}