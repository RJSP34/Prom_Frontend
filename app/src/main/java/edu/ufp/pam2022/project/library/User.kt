package edu.ufp.pam2022.project.library

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey var UserId: Int,
    @ColumnInfo(name = "Username") var Username: String,
    @ColumnInfo(name = "Email") var Email: String,
)
