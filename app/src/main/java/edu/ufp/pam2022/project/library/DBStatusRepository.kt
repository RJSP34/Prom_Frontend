package edu.ufp.pam2022.project.library

import androidx.lifecycle.LiveData

class DBStatusRepository(private val statusDao: DataBaseStatusDao) {

    val readAllData: LiveData<List<Status>> = statusDao.Status_getAll()

    suspend fun addStatus(status: Status){
        statusDao.Status_insertAll(status)
    }

    suspend fun Clear_Status(){
        statusDao.Status_delete()
    }
}