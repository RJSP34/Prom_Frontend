package edu.ufp.pam2022.project.library

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Backlog(
    @PrimaryKey var BackLogId: Int,
    @ColumnInfo(name = "MovieName") var MovieName: String,
    @ColumnInfo(name = "UserId") var UserId: Int,
    @ColumnInfo(name = "MovieId") var MovieId: Int,
    @ColumnInfo(name = "WatchedDate") var WatchedDate: String,
    @ColumnInfo(name = "StatusId") var StatusId: Int,
    @ColumnInfo(name = "rating") var rating: Int,
                      )
