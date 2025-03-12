package edu.ufp.pam2022.project.library

import androidx.lifecycle.LiveData

class DBBackLogRepository(private val BackLogDao: DataBaseBacklogDao) {

    var readAllData: LiveData<List<Backlog>> = BackLogDao.Backlog_getAll()

    suspend fun addBacklog(backlog: Backlog){
        BackLogDao.Backlog_insertAll(backlog)
        readAllData  = BackLogDao.Backlog_getAll()
    }

    suspend fun CLear_Backlog(){
        BackLogDao.Backlog_delete()
    }

    suspend fun Get_Backlog_By_Id(id:Int ){
      readAllData= BackLogDao.Backlog_getbyuserid(id)
    }

}