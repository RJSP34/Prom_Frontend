package edu.ufp.pam2022.project.library

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movie(
    @PrimaryKey var MovieId: Int,
    @ColumnInfo(name = "Name")  var Name: String,
    @ColumnInfo(name = "ReleaseDate") var ReleaseDate: String,
    @ColumnInfo(name = "ImbRating") var ImbRating: Int,
    @ColumnInfo(name = "runTime") var runTime: String,
    @ColumnInfo(name = "ageRatingId") var ageRatingId: String,
                 )
