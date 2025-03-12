package edu.ufp.pam2022.project.library

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DataBaseBacklogDao {

    @Query("SELECT * FROM Backlog")
    fun Backlog_getAll(): LiveData<List<Backlog>>

    @Query("SELECT * FROM Backlog where UserId= :userId")
    fun Backlog_getbyuserid(userId: Int): LiveData<List<Backlog>>

    @Insert
    fun Backlog_insertAll(vararg backlogs: Backlog)

    @Query("DELETE FROM Backlog")
    fun Backlog_delete()

}