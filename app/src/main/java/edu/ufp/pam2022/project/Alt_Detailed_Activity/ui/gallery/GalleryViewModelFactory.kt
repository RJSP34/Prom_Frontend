package edu.ufp.pam2022.project.Alt_Detailed_Activity.ui.gallery

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.ufp.pam2022.project.Alt_Detailed_Activity.ui.slideshow.SlideshowViewModel

class GalleryViewModelFactory (private val app: FragmentActivity) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GalleryViewModel::class.java)) {
            return GalleryViewModel(
                app
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}