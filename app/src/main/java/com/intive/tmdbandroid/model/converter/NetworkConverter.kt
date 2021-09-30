package com.intive.tmdbandroid.model.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intive.tmdbandroid.model.Network
import java.lang.reflect.Type

class NetworkConverter {
    @TypeConverter // note this annotation
    fun toOptionValuesList(genreString: String?): List<Network>? {
        if (genreString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<Network?>?>() {}.type
        return gson.fromJson<List<Network>>(genreString, type)
    }

    @TypeConverter
    fun fromOptionValuesList(genre: List<Network?>?): String? {
        if (genre == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Network?>?>() {}.type
        return gson.toJson(genre, type)
    }
}