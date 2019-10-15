package com.example.progresee.utils

import androidx.room.TypeConverter
import com.example.progresee.beans.Classroom
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.Moshi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
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

    companion object {
        @JvmStatic
        @TypeConverter
        fun fromString(value: String): Map<Long, Classroom> {
            val mapType = object : TypeToken<Map<Long, Classroom>>() {}.type
            return Gson().fromJson(value, mapType)
        }

        @TypeConverter
        @JvmStatic
        fun fromStringMap(map: Map<Long, Classroom>): String {
            val gson = Gson()
            return gson.toJson(map)
        }
    }



}