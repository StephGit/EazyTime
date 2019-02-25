package ch.bfh.mad.eazytime.di

import android.app.Application
import ch.bfh.mad.eazytime.geofence.GeoFenceAdapter
import ch.bfh.mad.eazytime.geofence.GeoFenceFragment
import ch.bfh.mad.eazytime.geofence.GeoFenceViewModel
import ch.bfh.mad.eazytime.geofence.detail.GeoFenceDetailActivity
import ch.bfh.mad.eazytime.homeScreenWidget.WidgetBroadCastReceiver
import ch.bfh.mad.eazytime.homeScreenWidget.WidgetProvider
import ch.bfh.mad.eazytime.projects.ProjectFragment
import ch.bfh.mad.eazytime.projects.addProject.AddProjectActivity
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

    fun inject(projectFragment: ProjectFragment)

    fun inject(addProjectActivity: AddProjectActivity)

    fun inject(homeScreenWidgetBroadCastReceiver: WidgetBroadCastReceiver)

    fun inject(widgetProvider: WidgetProvider)
    fun inject(geoFenceViewModel: GeoFenceViewModel)
    fun inject(geoFenceDetailActivity: GeoFenceDetailActivity)
    fun inject(geoFenceFragment: GeoFenceFragment)
    fun inject(geoFenceAdapter: GeoFenceAdapter)
}