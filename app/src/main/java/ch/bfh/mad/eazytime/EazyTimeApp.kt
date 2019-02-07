package ch.bfh.mad.eazytime

import android.app.Application
import ch.bfh.mad.eazytime.di.Injector

val Any.TAG: String
    get() {
        return "EazyTime"
    }
class EazyTimeApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Injector.init(this)
    }
}