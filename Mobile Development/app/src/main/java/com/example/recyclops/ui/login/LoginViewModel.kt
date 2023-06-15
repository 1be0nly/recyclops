package com.example.recyclops.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recyclops.repository.TokenPreferences
import kotlinx.coroutines.launch

class LoginViewModel (private val pref: TokenPreferences) :ViewModel() {

    fun saveToken(token: String) {
        viewModelScope.launch {
            pref.saveToken(token)
        }
    }

}