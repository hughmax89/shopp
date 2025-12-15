package com.aguerodev.shopp.data.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aguerodev.shopp.data.datasource.database.dao.ShoppDao
import com.aguerodev.shopp.data.datasource.database.entities.ProductEntity

@Database(entities = [ProductEntity::class], version = 1)
@TypeConverters(ListConverter::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun shoppDao(): ShoppDao
}