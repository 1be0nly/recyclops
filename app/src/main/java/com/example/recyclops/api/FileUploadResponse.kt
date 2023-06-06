package com.example.recyclops.api

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(

    @field: SerializedName("error")
    val error: String?,

    @field: SerializedName("imageUrl")
    val imageUrl: String?,

    @field: SerializedName("wasteType")
    val wasteType: String?,

)
