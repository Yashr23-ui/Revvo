package com.revvo

import android.app.Application
import com.revvo.di.AppContainer

/**
 * App-level Application class. Owns the [AppContainer] (single source of truth for
 * dependencies) for the process lifetime.
 *
 * Registered in AndroidManifest.xml via android:name=".RevvoApp".
 */
class RevvoApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer()
    }
}
