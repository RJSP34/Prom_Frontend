package edu.ufp.pam2022.project.library

import androidx.lifecycle.LiveData

class DBUserRepository(private val UserDao: DataBaseUserDao) {
        val readAllData: LiveData<List<User>> = UserDao.User_getAll()

        suspend fun addUser(User: User){
            UserDao.User_insertAll(User)
        }

        suspend fun DeleteUser(){
            UserDao.User_delete()
        }
}