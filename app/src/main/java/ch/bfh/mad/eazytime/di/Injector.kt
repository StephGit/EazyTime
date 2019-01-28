package ch.bfh.mad.eazytime.di

import android.app.Application
import ch.bfh.mad.eazytime.di.DaggerAppComponent.builder

object Injector {
    lateinit var appComponent : AppComponent
    fun init(application: Application) {
        appComponent = builder()
            .application(application)
            .build()
    }
}