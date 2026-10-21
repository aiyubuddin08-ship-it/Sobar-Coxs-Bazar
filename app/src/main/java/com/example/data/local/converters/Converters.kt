package com.example.data.local.converters

import androidx.room.TypeConverter
import com.example.data.model.BeachFlag
import com.example.data.model.ContentStatus
import com.example.data.model.DishItem
import com.example.data.model.RoomType
import com.example.data.model.WeatherForecastDay
import org.json.JSONArray
import org.json.JSONObject

class Converters {

    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        if (value == null) return "[]"
        val array = JSONArray()
        value.forEach { array.put(it) }
        return array.toString()
    }

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        if (value.isNullOrBlank()) return emptyList()
        return try {
            val array = JSONArray(value)
            val list = mutableListOf<String>()
            for (i in 0 until array.length()) {
                list.add(array.getString(i))
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    @TypeConverter
    fun fromContentStatus(status: ContentStatus?): String {
        return (status ?: ContentStatus.PUBLISHED).name
    }

    @TypeConverter
    fun toContentStatus(value: String?): ContentStatus {
        return try {
            if (value.isNullOrBlank()) ContentStatus.PUBLISHED else ContentStatus.valueOf(value)
        } catch (e: Exception) {
            ContentStatus.PUBLISHED
        }
    }

    @TypeConverter
    fun fromBeachFlag(flag: BeachFlag?): String {
        return (flag ?: BeachFlag.GREEN).name
    }

    @TypeConverter
    fun toBeachFlag(value: String?): BeachFlag {
        return try {
            if (value.isNullOrBlank()) BeachFlag.GREEN else BeachFlag.valueOf(value)
        } catch (e: Exception) {
            BeachFlag.GREEN
        }
    }

    @TypeConverter
    fun fromRoomTypeList(value: List<RoomType>?): String {
        if (value == null) return "[]"
        val array = JSONArray()
        value.forEach { room ->
            val obj = JSONObject().apply {
                put("name", room.name)
                put("pricePerNight", room.pricePerNight)
                put("capacity", room.capacity)
                put("bedType", room.bedType)
                put("hasOceanView", room.hasOceanView)
            }
            array.put(obj)
        }
        return array.toString()
    }

    @TypeConverter
    fun toRoomTypeList(value: String?): List<RoomType> {
        if (value.isNullOrBlank()) return emptyList()
        return try {
            val array = JSONArray(value)
            val list = mutableListOf<RoomType>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    RoomType(
                        name = obj.optString("name", ""),
                        pricePerNight = obj.optInt("pricePerNight", 0),
                        capacity = obj.optString("capacity", ""),
                        bedType = obj.optString("bedType", ""),
                        hasOceanView = obj.optBoolean("hasOceanView", false)
                    )
                )
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    @TypeConverter
    fun fromDishItemList(value: List<DishItem>?): String {
        if (value == null) return "[]"
        val array = JSONArray()
        value.forEach { dish ->
            val obj = JSONObject().apply {
                put("nameBn", dish.nameBn)
                put("nameEn", dish.nameEn)
                put("price", dish.price)
                put("isSpecial", dish.isSpecial)
            }
            array.put(obj)
        }
        return array.toString()
    }

    @TypeConverter
    fun toDishItemList(value: String?): List<DishItem> {
        if (value.isNullOrBlank()) return emptyList()
        return try {
            val array = JSONArray(value)
            val list = mutableListOf<DishItem>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    DishItem(
                        nameBn = obj.optString("nameBn", ""),
                        nameEn = obj.optString("nameEn", ""),
                        price = obj.optInt("price", 0),
                        isSpecial = obj.optBoolean("isSpecial", false)
                    )
                )
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    @TypeConverter
    fun fromWeatherForecastList(value: List<WeatherForecastDay>?): String {
        if (value == null) return "[]"
        val array = JSONArray()
        value.forEach { forecast ->
            val obj = JSONObject().apply {
                put("dayBn", forecast.dayBn)
                put("dayEn", forecast.dayEn)
                put("tempMax", forecast.tempMax)
                put("tempMin", forecast.tempMin)
                put("conditionBn", forecast.conditionBn)
            }
            array.put(obj)
        }
        return array.toString()
    }

    @TypeConverter
    fun toWeatherForecastList(value: String?): List<WeatherForecastDay> {
        if (value.isNullOrBlank()) return emptyList()
        return try {
            val array = JSONArray(value)
            val list = mutableListOf<WeatherForecastDay>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    WeatherForecastDay(
                        dayBn = obj.optString("dayBn", ""),
                        dayEn = obj.optString("dayEn", ""),
                        tempMax = obj.optString("tempMax", ""),
                        tempMin = obj.optString("tempMin", ""),
                        conditionBn = obj.optString("conditionBn", "")
                    )
                )
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }
}
