package edu.ufp.pam2022.project.Alt_Detailed_Activity

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.ufp.pam2022.project.Alt_Detailed_Activity.ui.slideshow.SlideshowViewModel

class DrawerMainViewModelFactory(private val app: AppCompatActivity) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DrawerMainViewModel::class.java)) {
            return DrawerMainViewModel(
                app
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}