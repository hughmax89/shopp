package com.aguerodev.shopp.data.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aguerodev.shopp.data.datasource.database.dao.ShoppDao
import com.aguerodev.shopp.data.datasource.database.entities.ProductEntity

@Database(entities = [ProductEntity::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun shoppDao(): ShoppDao
}