package com.app.incroyable.lockscreen.model

import com.google.gson.annotations.SerializedName

data class MultipleResource(

    @SerializedName("version")
    val version: Int,

    @SerializedName("package")
    val packageName: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("msg")
    val msg: String,

    @SerializedName("splashAppOpen")
    val splashAppOpen: Boolean,

    @SerializedName("data")
    val data: AppData,

    @SerializedName("wallpaper")
    val wallpaper: List<Int>
)
