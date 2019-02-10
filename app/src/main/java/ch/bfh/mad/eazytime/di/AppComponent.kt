package ch.bfh.mad.eazytime.di

import android.app.Application
import ch.bfh.mad.eazytime.geofence.GeoFenceViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance // Dagger injects in DependencyGraph
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
    fun inject(geoFenceViewModel: GeoFenceViewModel)

}