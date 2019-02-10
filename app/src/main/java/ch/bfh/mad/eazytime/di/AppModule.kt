package ch.bfh.mad.eazytime.di

import android.app.Application
import android.content.Context
import ch.bfh.mad.eazytime.data.AppDatabase
import ch.bfh.mad.eazytime.data.GeoFenceRepository
import ch.bfh.mad.eazytime.geofence.GeoFenceViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application) : Context = application

    @Provides
    @Singleton
    fun provideGeoFenceRepository() : GeoFenceRepository = GeoFenceRepository()

    @Provides
    fun provideGeoFenceViewModel(geoFenceViewModel: GeoFenceViewModel) : GeoFenceViewModel = geoFenceViewModel
}