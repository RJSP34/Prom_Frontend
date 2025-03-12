package edu.ufp.pam2022.project.listMovie

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ScrollingProjectActivityViewModelFactory(private val app: AppCompatActivity) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScrollingProjectActivityViewModel::class.java)) {
            return ScrollingProjectActivityViewModel(
                app
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}