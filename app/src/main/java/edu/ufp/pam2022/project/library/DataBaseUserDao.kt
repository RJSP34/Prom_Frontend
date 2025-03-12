package edu.ufp.pam2022.project.library

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DataBaseUserDao {

    @Query("SELECT * FROM User")
    fun User_getAll(): LiveData<List<User>>

    @Insert
    fun User_insertAll(vararg users: User)

    @Query("DELETE FROM User")
    fun User_delete()

}