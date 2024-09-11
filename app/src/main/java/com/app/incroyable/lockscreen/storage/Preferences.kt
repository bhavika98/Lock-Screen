package com.app.incroyable.lockscreen.storage

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.app.incroyable.lockscreen.model.QuestionData
import com.app.incroyable.lockscreen.utilities.wallpaperName
import java.lang.reflect.Type

private const val preferences = "preferences"

const val prefServerWallpaper = "prefServerWallpaper"
const val prefTheme = "prefTheme"
const val prefWallpaper = "prefWallpaper"
const val prefTimeFormat = "prefTimeFormat"
const val prefLockSet = "prefLockSet"
const val prefQuestionList = "prefQuestionList"
const val prefSavedQuestion = "prefSavedQuestion"
const val prefSavedAnswer = "prefSavedAnswer"

const val prefPinEnable = "prefPinEnable"
const val prefPatternEnable = "prefPatternEnable"
const val prefPinPassword = "prefPinPassword"
const val prefPatternPassword = "prefPatternPassword"
const val prefLockStatus = "prefLockStatus"
const val prefAlreadyLock = "prefAlreadyLock"
const val prefLockHint = "prefLockHint"

const val prefRate = "prefRate"
const val prefLastDate = "prefLastDate"
const val prefSecondTime = "prefSecondTime"

fun Context.setPreferences(Key: String, Value: Any) {
    val sharedPreferences = this.getSharedPreferences(
        preferences,
        Activity.MODE_PRIVATE
    )
    val editor = sharedPreferences.edit()
    when (Value) {
        is Int -> {
            editor.putInt(Key, Value)
        }

        is String -> {
            editor.putString(Key, Value)
        }

        is Boolean -> {
            editor.putBoolean(Key, Value)
        }
    }
    editor.commit()
}

fun Context.getDefaultPreferences(
    Key: String
): Any {
    val sharedPreferences = this.getSharedPreferences(
        preferences,
        Activity.MODE_PRIVATE
    )
    val result: Any = when (Key) {
        prefTheme -> {
            sharedPreferences.getInt(Key, 1) //Default Theme Style is : 1 [0 1 2]
        }

        prefWallpaper -> {
            sharedPreferences.getString(Key, "${wallpaperName}2") //Default Wallpaper is : 1 [1 2 3]
        }

        prefTimeFormat, prefLockHint -> {
            sharedPreferences.getBoolean(
                Key,
                true
            ) //Default Time Format is : true = 12hrs [false = 24hrs]
        }

        prefLockSet, prefPinEnable, prefPatternEnable, prefLockStatus, prefAlreadyLock, prefRate, prefSecondTime -> {
            sharedPreferences.getBoolean(
                Key,
                false
            ) //Default Value is : false = Password not Set [true = Password Set]
        }

        prefSavedQuestion -> {
            sharedPreferences.getInt(
                Key,
                0
            ) //Default Value is : 0 = Question not Set [>0 = Question Set]
        }

        prefSavedAnswer, prefPinPassword, prefPatternPassword, prefLastDate -> {
            sharedPreferences.getString(Key, "") //Default Value is : "" = Answer not Set
        }

        else -> {}
    }!!
    return result
}

fun Context.setPrefQuestionData(arrayList: ArrayList<QuestionData>) {
    val sharedPreferences = this.getSharedPreferences(
        preferences,
        Activity.MODE_PRIVATE
    )
    val editor = sharedPreferences.edit()
    val gson = Gson()
    val json: String = gson.toJson(arrayList)
    editor.putString(prefQuestionList, json)
    editor.commit()
}

fun Context.getPrefQuestionData(): ArrayList<QuestionData> {
    val albumSettingArrayList: ArrayList<QuestionData>
    val sharedPreferences = this.getSharedPreferences(
        preferences,
        Activity.MODE_PRIVATE
    )
    val gson = Gson()
    val json =
        sharedPreferences.getString(prefQuestionList, "")
    albumSettingArrayList = if (json!!.isEmpty()) {
        ArrayList()
    } else {
        val type: Type = object : TypeToken<List<QuestionData>>() {}.type
        gson.fromJson(json, type)
    }
    return albumSettingArrayList
}

fun Context.saveWallpaperList(intList: List<Int>) {
    val sharedPreferences = this.getSharedPreferences(
        preferences,
        Activity.MODE_PRIVATE
    )
    val editor = sharedPreferences.edit()

    val gson = Gson()
    val json = gson.toJson(intList)

    editor.putString(prefServerWallpaper, json)
    editor.apply()
}

fun Context.getWallpaperList(): List<Int> {
    val sharedPreferences = this.getSharedPreferences(
        preferences,
        Activity.MODE_PRIVATE
    )
    val json = sharedPreferences.getString(prefServerWallpaper, null)

    val gson = Gson()
    return gson.fromJson(json, object : TypeToken<List<Int>>() {}.type)
}
