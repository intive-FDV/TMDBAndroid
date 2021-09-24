package com.intive.tmdbandroid.model.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class IntConverter {
    @TypeConverter
    fun toOptionValuesList(intString: String?): List<Int>? {
        if (intString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<Int?>?>() {}.type
        return gson.fromJson<List<Int>>(intString, type)
    }

    @TypeConverter
    fun fromOptionValuesList(int: List<Int?>?): String? {
        if (int == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Int?>?>() {}.type
        return gson.toJson(int, type)
    }
}