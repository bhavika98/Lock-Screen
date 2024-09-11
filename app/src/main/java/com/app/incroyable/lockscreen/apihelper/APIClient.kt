package com.app.incroyable.lockscreen.apihelper

import android.util.Base64
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.StandardCharsets

var baseUrl = "aHR0cDovL2dyYW5rd2luLmNvbS9wcm9qZWN0Lw=="
var updateUrl = "aW5kcGIvc3lzdGVtX2xvY2tzY3JlZW5zLmpzb24="
var wallpaperUrl = "aW5kcGIvd2FsbHBhcGVyLw=="

internal object APIClient {
    private var retrofit: Retrofit? = null

    val client: Retrofit?
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
            retrofit = Retrofit.Builder()
                .baseUrl(decodeUrl(baseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit
        }
}

fun decodeUrl(url: String): String {
    val decodedBytes: ByteArray = Base64.decode(url, Base64.DEFAULT)
    return String(decodedBytes, StandardCharsets.UTF_8)
}