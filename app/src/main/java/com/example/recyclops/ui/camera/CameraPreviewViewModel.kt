package com.example.recyclops.ui.camera

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.recyclops.api.ApiConfig
import com.example.recyclops.api.FileUploadResponse
import com.example.recyclops.api.ResponseInterface
import com.example.recyclops.api.UploadImageConfirmedResponse
import com.example.recyclops.data.TrashScanned
import com.example.recyclops.repository.TokenPreferences
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("NullSafeMutableLiveData")
class CameraPreviewViewModel (private val pref: TokenPreferences) : ViewModel() {

    val scannedImage = MutableLiveData<FileUploadResponse>()
    val scannedImage2 = MutableLiveData<UploadImageConfirmedResponse?>()

    private var _scannedTrash = MutableLiveData<List<TrashScanned>>()
    val scannedTrash: LiveData<List<TrashScanned>>
        get() = _scannedTrash



    fun addListTrashScanned(listTrashScanned: List<TrashScanned>) {
        _scannedTrash.value = listTrashScanned
    }

    fun uploadImageConfirmation(token: String, wasteType: String, weight: Int,uniqueId: String, context: Context){
        val apiService = ApiConfig().getApiService()
        val uploadImageRequest = apiService.uploadImageConfirmed(token,wasteType,uniqueId,weight)
        uploadImageRequest.enqueue(object : Callback<UploadImageConfirmedResponse>{
            override fun onResponse(
                call: Call<UploadImageConfirmedResponse>,
                response: Response<UploadImageConfirmedResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful){
                    if (responseBody != null)
                        scannedImage2.postValue(responseBody)
                }
            }
            override fun onFailure(call: Call<UploadImageConfirmedResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun increaseQuantity(id: Int) {
        val list = _scannedTrash.value?.toMutableList()
        val index = list?.indexOfFirst { it.id == id }
        if (index != null && index >= 0) {
            list[index].quantity += 1
            _scannedTrash.value = list
        }
    }

    fun decreaseQuantity(id: Int) {
        val list = _scannedTrash.value?.toMutableList()
        val index = list?.indexOfFirst { it.id == id }
        if (index != null && index >= 0) {
            if (list[index].quantity == 1)  {
                _scannedTrash.value = list
            } else {
                list[index].quantity -= 1
                _scannedTrash.value = list
            }
        }
    }

    fun deleteItem(id: Int) {
        val list = _scannedTrash.value?.toMutableList()
        val index = list?.indexOfFirst { it.id == id }
        if (index != null ) {
            list.removeAt(index)
            _scannedTrash.value = list
        }
    }

    fun uploadImage(
        token: String,
        imageMultipartBody: MultipartBody.Part,
        context: Context
    ) {
        val apiService = ApiConfig().getApiService()
        val uploadImageRequest = apiService.uploadImage(token,imageMultipartBody)

        uploadImageRequest.enqueue(object : Callback<FileUploadResponse> {
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful) {
                    if (responseBody != null) {
                        Log.d("Success", responseBody.error.toString())
                        Log.d("ImageUrl", responseBody.imageUrl.toString())
                        Log.d("WasteType", responseBody.wasteType.toString())
                        Log.d("Confidence", responseBody.confidence.toString())
                        //ResponseInterface.fileUploadResponseAdded(responseBody)
                        scannedImage.postValue(responseBody)
                    }
                } else {
                    Log.d("Failure", responseBody?.error.toString())
                }
            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                Log.d("Failure", t.message.toString())
            }
        })
    }

    fun getResult(): LiveData<FileUploadResponse>{
        return scannedImage
    }

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }
}