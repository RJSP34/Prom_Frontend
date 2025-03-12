package edu.ufp.pam2022.project.Alt_Detailed_Activity

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ufp.pam2022.project.library.*
import edu.ufp.pam2022.project.services.repositoryVolley.VolleyRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DrawerMainViewModel(App: AppCompatActivity) : ViewModel() {
    val _Users=MutableLiveData<List<User>>()
    var User : LiveData<List<User>> =_Users
    private var repository: DBUserRepository
    private var repository_backlog: DBBackLogRepository
    init {
        val Userdao = AppDatabase.getDatabase(App).databaseUserDao()
        val backlogdao = AppDatabase.getDatabase(App).databaseBacklogDao()
        repository_backlog=DBBackLogRepository(backlogdao)
        repository = DBUserRepository(Userdao)
        User = repository.readAllData
    }

    fun Rest_Table_Users(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.DeleteUser()
            repository.addUser(user)
            repository_backlog.CLear_Backlog()
        }
    }

}