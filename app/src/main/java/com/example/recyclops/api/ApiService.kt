package com.example.recyclops.api

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Multipart
    @POST("upload")
    fun uploadImage(
        @Header("authorization") token: String,
        @Part image: MultipartBody.Part,
    ): Call<FileUploadResponse>

}