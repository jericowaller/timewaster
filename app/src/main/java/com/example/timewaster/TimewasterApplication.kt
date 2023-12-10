package com.example.timewaster

import android.app.Application
import com.example.timewaster.data.DBRoomDatabase

class TimewasterApplication : Application(){
    val database: DBRoomDatabase by lazy { DBRoomDatabase.getDatabase(this) }
}

