package com.intive.tmdbandroid.model.converter

import androidx.room.TypeConverter
import com.intive.tmdbandroid.model.CreatedBy
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intive.tmdbandroid.model.Genre
import java.lang.reflect.Type


class GenreConverter {
   @TypeConverter // note this annotation
    fun toOptionValuesList(genreString: String?): List<Genre>? {
        if (genreString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<Genre?>?>() {}.type
        return gson.fromJson<List<Genre>>(genreString, type)
    }

    @TypeConverter
    fun fromOptionValuesList(genre: List<Genre?>?): String? {
        if (genre == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Genre?>?>() {}.getType()
        return gson.toJson(genre, type)
    }
}