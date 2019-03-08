package ch.bfh.mad.eazytime.di

import android.content.Context
import ch.bfh.mad.eazytime.data.AppDatabase
import ch.bfh.mad.eazytime.data.dao.GeoFenceDao
import ch.bfh.mad.eazytime.data.repo.GeoFenceRepo
import ch.bfh.mad.eazytime.geofence.GeoFenceRecyclerAdapter
import ch.bfh.mad.eazytime.geofence.GeoFenceService
import ch.bfh.mad.eazytime.geofence.GeofenceErrorMessages
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class GeoFenceModule {
    @Provides
    @Singleton
    fun provideGeoFenceDao(database: AppDatabase): GeoFenceDao = database.geoFenceDao()

    @Provides
    @Singleton
    fun provideGeoFenceRepository(geoFenceDao: GeoFenceDao): GeoFenceRepo =
        GeoFenceRepo(geoFenceDao)

    @Provides
    @Singleton
    fun provideGeoFenceService(
        context: Context,
        geoFenceRepo: GeoFenceRepo,
        geofenceErrorMessages: GeofenceErrorMessages
    ):
            GeoFenceService = GeoFenceService(context, geoFenceRepo, geofenceErrorMessages)

    @Provides
    @Singleton
    fun provideGeoFenceRecyclerAdapter(geoFenceService: GeoFenceService):
            GeoFenceRecyclerAdapter = GeoFenceRecyclerAdapter(geoFenceService)

    @Provides
    @Singleton
    fun provideGeoFenceErrorMessages(context: Context): GeofenceErrorMessages = GeofenceErrorMessages(context)
}