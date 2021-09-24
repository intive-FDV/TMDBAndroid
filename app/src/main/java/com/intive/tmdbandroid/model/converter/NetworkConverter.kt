package com.intive.tmdbandroid.model.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intive.tmdbandroid.model.Network
import java.lang.reflect.Type

class NetworkConverter {
    @TypeConverter // note this annotation
    fun toOptionValuesList(netWorkUrl: String?): List<Network>? {
        if (netWorkUrl == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<Network?>?>() {}.type
        return gson.fromJson<List<Network>>(netWorkUrl, type)
    }

    @TypeConverter
    fun fromOptionValuesList(network: List<Network?>?): String? {
        if (network == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Network?>?>() {}.type
        return gson.toJson(network, type)
    }
}