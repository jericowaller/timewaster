package com.example.timewaster.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DB(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "points")
    val points: Int,
    @ColumnInfo(name = "clickers")
    val clickers: Int,
    @ColumnInfo(name = "superClickers")
    val superClickers: Int
)
