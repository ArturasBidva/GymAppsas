package com.example.gymappsas.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class Converters {
    private val gson: Gson

    init {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(LocalDate::class.java, LocalDateSerializer())
        gsonBuilder.registerTypeAdapter(LocalDate::class.java, LocalDateDeserializer())
        gsonBuilder.registerTypeAdapter(LocalTime::class.java, LocalTimeSerializer())
        gsonBuilder.registerTypeAdapter(LocalTime::class.java, LocalTimeDeserializer())
        gson = gsonBuilder.create()
    }

    internal class LocalDateSerializer : JsonSerializer<LocalDate?> {
        override fun serialize(
            localDate: LocalDate?,
            srcType: Type?,
            context: JsonSerializationContext?
        ): JsonElement {
            return JsonPrimitive(formatter.format(localDate))
        }

        companion object {
            private val formatter = DateTimeFormatter.ofPattern("d-MMM-yyyy")
        }
    }

    internal class LocalTimeSerializer : JsonSerializer<LocalTime?> {
        override fun serialize(
            localTime: LocalTime?,
            srcType: Type?,
            context: JsonSerializationContext?
        ): JsonElement {
            return JsonPrimitive(formatter.format(localTime))
        }

        companion object {
            private val formatter = DateTimeFormatter.ofPattern("HH:mm")
        }
    }

    internal class LocalTimeDeserializer : JsonDeserializer<LocalTime> {
        @Throws(JsonParseException::class)
        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): LocalTime {
            return LocalTime.parse(json.asString, DateTimeFormatter.ofPattern("HH:mm"))
        }
    }

    internal class LocalDateDeserializer : JsonDeserializer<LocalDate> {
        @Throws(JsonParseException::class)
        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): LocalDate {
            return LocalDate.parse(json.asString, DateTimeFormatter.ofPattern("d-MMM-yyyy"))
        }
    }

    @TypeConverter
    fun toDate(dateString: String?): LocalDate? {
        return if (dateString == null) {
            null
        } else {
            LocalDate.parse(dateString)
        }
    }

    @TypeConverter
    fun toTime(timeString: String?): LocalTime? {
        return if (timeString == null) {
            null
        } else {
            LocalTime.parse(timeString)
        }
    }

    @TypeConverter
    fun toTimeString(time: LocalTime?): String? {
        return time?.toString()
    }


    @TypeConverter
    fun toDateString(date: LocalDate?): String? {
        return date?.toString()
    }

}



