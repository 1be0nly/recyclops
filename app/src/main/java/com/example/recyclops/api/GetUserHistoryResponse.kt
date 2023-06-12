package com.example.recyclops.api

import com.example.recyclops.data.UserHistoryItem
import com.google.gson.annotations.SerializedName

data class GetUserHistoryResponse(

    @field: SerializedName("userHistory")
    val userHistory: ArrayList<UserHistoryItem>

)
