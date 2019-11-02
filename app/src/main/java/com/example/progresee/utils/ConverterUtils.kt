package com.example.progresee.utils

import androidx.room.TypeConverter
import com.example.progresee.beans.Classroom
import com.example.progresee.beans.DateCreated
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.util.*

class ConverterUtils {


    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun restoreList(listOfString: String): List<String> {
        return Gson().fromJson(listOfString, object : TypeToken<List<String>>() {
        }.type)
    }

    @TypeConverter
    fun saveList(listOfString: List<String>): String {
        return Gson().toJson(listOfString)
    }

    @TypeConverter
    fun fromDateCreated(dateCreated: DateCreated?): String? {
        if (dateCreated == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<DateCreated>() {
        }.type
        return gson.toJson(dateCreated, type)
    }

    @TypeConverter
    fun toDateCreated(dateCreated: String?): DateCreated? {
        if (dateCreated == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<DateCreated>() {
        }.type
        return gson.fromJson<DateCreated>(dateCreated, type)
    }


    @TypeConverter
    fun fromString(value: String): Map<String, String> {
        val mapType = object : TypeToken<Map<String, String>>() {

        }.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun fromStringMap(map: Map<String, String>): String {
        val gson = Gson()
        return gson.toJson(map)
    }

}