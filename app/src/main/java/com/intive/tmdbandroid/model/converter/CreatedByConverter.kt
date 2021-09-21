package com.intive.tmdbandroid.model.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intive.tmdbandroid.model.CreatedBy
import java.lang.reflect.Type


class CreatedByConverter {
   @TypeConverter // note this annotation
    fun toOptionValuesList(createdByString: String?): List<CreatedBy>? {
        if (createdByString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<CreatedBy?>?>() {}.type
        return gson.fromJson<List<CreatedBy>>(createdByString, type)
    }

    @TypeConverter
    fun fromOptionValuesList(createdBy: List<CreatedBy?>?): String? {
        if (createdBy == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<CreatedBy?>?>() {}.type
        return gson.toJson(createdBy, type)
    }
}