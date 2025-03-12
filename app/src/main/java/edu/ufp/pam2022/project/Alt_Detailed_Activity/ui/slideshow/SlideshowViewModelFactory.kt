package edu.ufp.pam2022.project.Alt_Detailed_Activity.ui.slideshow

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SlideshowViewModelFactory(private val app: FragmentActivity) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SlideshowViewModel::class.java)) {
                return SlideshowViewModel(
                    app
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
    }
}