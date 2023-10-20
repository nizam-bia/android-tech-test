package com.bridge.androidtechnicaltest.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PupilUi(
    val pupilId: Long,
    val pupilName: String,
    val country: String
) : Parcelable