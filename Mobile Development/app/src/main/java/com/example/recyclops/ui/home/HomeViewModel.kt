package com.example.recyclops.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recyclops.api.ApiConfig
import com.example.recyclops.api.GetUserHistoryResponse
import com.example.recyclops.api.GetUserPointResponse
import com.example.recyclops.api.UploadImageConfirmedResponse
import com.example.recyclops.data.UserHistoryItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _point = MutableLiveData<GetUserPointResponse?>()
    val point
        get() = _point

    private val listHistoryUser = MutableLiveData<ArrayList<UserHistoryItem>>()


    fun getUserPoint(token: String){
        val apiService = ApiConfig().getApiService()
        val uploadImageRequest = apiService.getUserPoint(token)
        uploadImageRequest.enqueue(object : Callback<GetUserPointResponse>{
            override fun onResponse(
                call: Call<GetUserPointResponse>,
                response: Response<GetUserPointResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful){
                    if (responseBody != null){
                        _point.value = responseBody
                    }
                }
            }

            override fun onFailure(call: Call<GetUserPointResponse>, t: Throwable) {
                Log.d("Failure", t.message.toString())
            }
        })
    }

    fun setUserHistory(token :String){
        val apiService = ApiConfig().getApiService()
        val uploadImageRequest = apiService.getUserHistory(token)
        uploadImageRequest.enqueue(object : Callback<GetUserHistoryResponse>{
            override fun onResponse(
                call: Call<GetUserHistoryResponse>,
                response: Response<GetUserHistoryResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful){
                    if (responseBody != null){
                        listHistoryUser.postValue(responseBody.userHistory)
                    }
                }
            }

            override fun onFailure(call: Call<GetUserHistoryResponse>, t: Throwable) {
                Log.d("Failure", t.message.toString())
            }
        })
    }

    fun getUserHistory(): LiveData<ArrayList<UserHistoryItem>>{
        return listHistoryUser
    }
}