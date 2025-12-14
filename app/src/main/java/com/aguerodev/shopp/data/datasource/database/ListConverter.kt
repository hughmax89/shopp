package com.aguerodev.shopp.data.datasource.database

import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import java.lang.reflect.Type
import com.google.gson.Gson

class ListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromList(list: List<String>?): String {
        return gson.toJson(list.orEmpty())
    }

    @TypeConverter
    fun toList(jsonString: String?): List<String> {
        if (jsonString.isNullOrEmpty()) {
            return emptyList()
        }

        val listType: Type = object : TypeToken<List<String>>() {}.type

        return gson.fromJson(jsonString, listType)
    }
}