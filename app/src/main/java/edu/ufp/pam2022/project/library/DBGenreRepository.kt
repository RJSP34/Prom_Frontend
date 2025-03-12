package edu.ufp.pam2022.project.library

import androidx.lifecycle.LiveData

class DBGenreRepository(private val GenreDao: DataBaseGenreDao) {

    val readAllData: LiveData<List<Genre>> = GenreDao.Genre_getAll()

    suspend fun addGenre(genre: Genre){
        GenreDao.Genre_insertAll(genre)
    }

    suspend fun Clear_Genre(){
        GenreDao.Genre_delete()
    }
}