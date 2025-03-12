package edu.ufp.pam2022.project.library

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DataBaseStatusDao {

    @Query("SELECT * FROM Status")
    fun Status_getAll(): LiveData<List<Status>>

    @Insert
    fun Status_insertAll(vararg status: Status)

    @Query("DELETE FROM Status")
    fun Status_delete()

}