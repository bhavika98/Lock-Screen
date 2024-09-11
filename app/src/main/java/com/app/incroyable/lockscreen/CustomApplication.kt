package com.app.incroyable.lockscreen

import android.app.Application
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig

class CustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        //Downloader
        val config: PRDownloaderConfig = PRDownloaderConfig.newBuilder()
            .setDatabaseEnabled(true)
            .build()
        PRDownloader.initialize(this, config)
    }
}