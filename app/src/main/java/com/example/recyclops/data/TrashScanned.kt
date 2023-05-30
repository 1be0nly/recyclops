package com.example.recyclops.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrashScanned (
    val id : Int,
    val name : String,
    var quantity : Int,
    val imageSampah : Int
) : Parcelable