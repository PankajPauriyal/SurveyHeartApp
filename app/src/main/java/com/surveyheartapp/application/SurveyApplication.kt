package com.surveyheartapp.application

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration

class SurveyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        // Initialize Realm
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("default.realm")
            .build()
        Realm.setDefaultConfiguration(config)
    }


    companion object {
        @JvmField
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null

        fun getContext(): Context {
            return context!!
        }

    }

}