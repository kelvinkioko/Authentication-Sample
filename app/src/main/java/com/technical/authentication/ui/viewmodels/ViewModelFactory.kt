package com.technical.authentication.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val loginViewModel: LoginViewModel,
    private val mainActivityViewModel: MainActivityViewModel
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java!!)) {
            return loginViewModel as T
        }
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java!!)) {
            return mainActivityViewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}