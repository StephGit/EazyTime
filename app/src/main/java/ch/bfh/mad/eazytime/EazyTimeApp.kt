package ch.bfh.mad.eazytime

import android.app.Application
import ch.bfh.mad.eazytime.di.Injector

class EazyTimeApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Injector.init(this)
    }
}