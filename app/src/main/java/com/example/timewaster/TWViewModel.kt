package com.example.timewaster

import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.timewaster.data.DB
import com.example.timewaster.data.DBDao
import kotlinx.coroutines.launch
import kotlin.io.path.createTempDirectory

class TWViewModel(private val DBDao: DBDao) : ViewModel() {
    companion object {
        var currentht = HandlerThread("clickerThread")
    }


    private fun insertItem(item: DB) {
        viewModelScope.launch {
            DBDao.insert(item)
        }
    }

    private fun getNewItemEntry(points: String, clickers: String, superClickers: String): DB {
        return DB(
            points = points.toInt(),
            clickers = clickers.toInt(),
            superClickers = superClickers.toInt()
        )
    }

    fun addNewItem(points: String, clickers: String, superClickers: String) {
        val newItem = getNewItemEntry(points, clickers, superClickers)
        insertItem(newItem)
    }

    private fun getUpdatedItemEntry(
        id: Int,
        points: String,
        clickers: String,
        superClickers: String
    ): DB {
        return DB(
            id = id,
            points = points.toInt(),
            clickers = clickers.toInt(),
            superClickers = superClickers.toInt()
        )
    }

    fun addPoints(DB: DB, pts: Int): DB {
        currentht.quit()
        val newDB = getUpdatedItemEntry(DB.id, (DB.points + pts).toString(), DB.clickers.toString(), DB.superClickers.toString())
        viewModelScope.launch {
            DBDao.update(newDB)
        }
        val x = createClickerThread()
        clickerTick(x, newDB)
        return newDB
    }

    fun buyClicker(DB: DB) {
        if (DB.points >= 100) {
            currentht.quit()
            val newDB = getUpdatedItemEntry(
                DB.id,
                (DB.points - 100).toString(),
                (DB.clickers + 1).toString(),
                DB.superClickers.toString()
            )
            viewModelScope.launch {
                DBDao.update(newDB)
            }
            val x = createClickerThread()
            clickerTick(x, newDB)
        }
    }

    fun buySuperClicker(DB: DB) {
        if (DB.points >= 1000) {
            currentht.quit()
            val newDB = getUpdatedItemEntry(
                DB.id,
                (DB.points - 1000).toString(),
                DB.clickers.toString(),
                (DB.superClickers + 1).toString()
            )
            viewModelScope.launch {
                DBDao.update(newDB)
            }
            val x = createClickerThread()
            clickerTick(x, newDB)
        }
    }


    fun retrieveItem(id: Int): LiveData<DB> {
        return DBDao.getDB(id).asLiveData()
    }

    fun clickerTick(ht: HandlerThread = HandlerThread("clickerThread"), DB: DB) {
        ht.start()
        currentht = ht
        val mainHandler = Handler(ht.looper)
        var x = DB
        mainHandler.postDelayed(object : Runnable {
            override fun run() {
                x = addPoints(x, x.clickers + (x.superClickers * 10))
                mainHandler.postDelayed(this, 1000)
            }
        }, 1000)
    }

    fun createClickerThread(): HandlerThread {
        return HandlerThread("clickerThread")
    }

    fun test(db: DB) {
        if (currentht.isAlive()) {
            currentht.quit()
        } else {
            clickerTick(currentht, db)
        }
    }

    fun win(DB: DB) {
        currentht.quit()
        if (DB.points >= 1000000) {
            val newDB = getUpdatedItemEntry(DB.id, 0.toString(), 0.toString(), 0.toString())
            viewModelScope.launch {
                DBDao.update(newDB)
            }

        }
    }


}

class TWViewModelFactory(private val itemDao: DBDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TWViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TWViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
