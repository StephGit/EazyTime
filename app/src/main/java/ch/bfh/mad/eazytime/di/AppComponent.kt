package ch.bfh.mad.eazytime.di

import android.app.Application
import ch.bfh.mad.eazytime.EazyTimeActivity
import ch.bfh.mad.eazytime.geofence.GeoFenceEmptyFragment
import ch.bfh.mad.eazytime.geofence.GeoFenceFragment
import ch.bfh.mad.eazytime.geofence.receiver.BootReceiver
import ch.bfh.mad.eazytime.geofence.receiver.GeoFenceReceiver
import ch.bfh.mad.eazytime.remoteViews.RemoteViewBroadCastReceiver
import ch.bfh.mad.eazytime.remoteViews.homeScreenWidget.WidgetProvider
import ch.bfh.mad.eazytime.remoteViews.notification.ScreenActionService
import ch.bfh.mad.eazytime.projects.ProjectFragment
import ch.bfh.mad.eazytime.projects.addProject.AddProjectActivity
import ch.bfh.mad.eazytime.remoteViews.RemoteViewButtonUtil
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

    fun inject(eazyTimeActivity: EazyTimeActivity)
    fun inject(projectFragment: ProjectFragment)
    fun inject(addProjectActivity: AddProjectActivity)
    fun inject(remoteViewBroadCastReceiver: RemoteViewBroadCastReceiver)
    fun inject(widgetProvider: WidgetProvider)
    fun inject(geoFenceFragment: GeoFenceFragment)
    fun inject(geoFenceEmptyFragment: GeoFenceEmptyFragment)
    fun inject(geoFenceReceiver: GeoFenceReceiver)
    fun inject(remoteViewButtonUtil: RemoteViewButtonUtil)
    fun inject(screenActionService: ScreenActionService)
    fun inject(bootReceiver: BootReceiver)
}