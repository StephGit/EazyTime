package ch.bfh.mad.eazytime.di

import android.app.Application
import ch.bfh.mad.eazytime.EazyTimeActivity
import ch.bfh.mad.eazytime.calendar.CalendarFragment
import ch.bfh.mad.eazytime.calendar.detail.CalendarDetailActivity
import ch.bfh.mad.eazytime.geofence.GeoFenceFragment
import ch.bfh.mad.eazytime.geofence.detail.GeoFenceDetailActivity
import ch.bfh.mad.eazytime.geofence.receiver.BootBroadcastReceiver
import ch.bfh.mad.eazytime.geofence.receiver.GeoFenceTransitionsJobIntentService
import ch.bfh.mad.eazytime.projects.ProjectFragment
import ch.bfh.mad.eazytime.projects.addProject.AddProjectActivity
import ch.bfh.mad.eazytime.remoteViews.RemoteViewBroadCastReceiver
import ch.bfh.mad.eazytime.remoteViews.RemoteViewButtonUtil
import ch.bfh.mad.eazytime.remoteViews.homeScreenWidget.WidgetProvider
import ch.bfh.mad.eazytime.remoteViews.notification.ScreenActionReceiver
import ch.bfh.mad.eazytime.remoteViews.notification.ScreenActionService
import ch.bfh.mad.eazytime.util.BurnoutProtectorService
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
    fun inject(calendarFragment: CalendarFragment)
    fun inject(calendarDetailActivity: CalendarDetailActivity)
    fun inject(remoteViewBroadCastReceiver: RemoteViewBroadCastReceiver)
    fun inject(widgetProvider: WidgetProvider)
    fun inject(geoFenceFragment: GeoFenceFragment)
    fun inject(geoFenceDetailActivity: GeoFenceDetailActivity)
    fun inject(remoteViewButtonUtil: RemoteViewButtonUtil)
    fun inject(screenActionService: ScreenActionService)
    fun inject(screenActionReceiver: ScreenActionReceiver)
    fun inject(geoFenceTransitionsJobIntentService: GeoFenceTransitionsJobIntentService)
    fun inject(bootBroadcastReceiverTask: BootBroadcastReceiver.BootReceiverTask)
    fun inject(burnoutProtectorService: BurnoutProtectorService)
}