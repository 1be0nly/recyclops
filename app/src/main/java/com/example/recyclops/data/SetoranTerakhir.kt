package com.example.recyclops.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SetoranTerakhir(
    val name : String,
    val quantity : String,
    val bankSampah : String,
    val imageSampah : Int
): Parcelable

