package com.rakshit.one

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.sleek.construction.config.Config
import android.sleek.construction.di.apiModule
import android.sleek.construction.di.networkModule
import android.sleek.construction.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ApplicationClass : Application() {

    // Customize the notification channel as you wish. This is only for a bare minimum example
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(
                notificationChannelID,
                "TestApp Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }


    companion object {
        const val notificationChannelID = "TestChannel"

        private lateinit var instance: ApplicationClass

        @JvmStatic
        fun getApp() = instance
    }

    override fun onCreate() {
        super.onCreate()

        Config.init(this@ApplicationClass)

        startKoin {
            androidContext(this@ApplicationClass)
            modules(
                listOf(
                    networkModule,
                    apiModule,
                    viewModelModule
                )
            )
        }
        createNotificationChannel()
    }
}
