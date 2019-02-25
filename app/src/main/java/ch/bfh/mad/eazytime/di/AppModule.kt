package ch.bfh.mad.eazytime.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import ch.bfh.mad.eazytime.data.AppDatabase
import ch.bfh.mad.eazytime.data.GeoFenceRepository
import ch.bfh.mad.eazytime.data.dao.GeoFenceDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(includes = [ViewModelModule::class])
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    fun provideGeoFenceRepository(geoFenceDao: GeoFenceDao): GeoFenceRepository = GeoFenceRepository(geoFenceDao)

    @Provides
    @Singleton
    fun providesEazyTimeColorUtil(context: Context) =  EazyTimeColorUtil(context)

    @Provides
    @Singleton
    fun providesProjectSaveOrUpdateService(projectDao: ProjectDao) = ProjectSaveOrUpdateService(projectDao)

    @Provides
    @Singleton
    fun providesProjectProviderService(projectRepo: ProjectRepo, timeSlotRepo: TimeSlotRepo) = ProjectProviderService(projectRepo, timeSlotRepo)

    @Provides
    fun provideAppDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "EazyTimeDB").build()

    @Provides
    @Singleton
    fun provideTimeSlotDao(database: AppDatabase) = database.timeSlotDao()

    @Provides
    @Singleton
    fun provideProjectDao(database: AppDatabase) = database.projectDao()

    @Provides
    @Singleton
    fun provideWorkDayDao(database: AppDatabase) = database.workDayDao()

    @Provides
    fun provideGeoFenceViewModel(geoFenceViewModel: GeoFenceViewModel) : GeoFenceViewModel = geoFenceViewModel
    @Provides
    @Singleton
    fun provideTimerService(timeSlotRepo: TimeSlotRepo, projectDao: ProjectDao, workDayRepo: WorkDayRepo) = TimerService(timeSlotRepo, projectDao, workDayRepo)

    @Provides
    @Singleton
    fun provideTimeSlotRepo(timeSlotDao: TimeSlotDao) = TimeSlotRepo(timeSlotDao)

    @Provides
    @Singleton
    fun provideProjectRepo(projectDao: ProjectDao) = ProjectRepo(projectDao)

    @Provides
    @Singleton
    fun provideWorkdayRepo(workDayDao: WorkDayDao) = WorkDayRepo(workDayDao)

    @Provides
    @Singleton
    fun provideWidgetProviderUtils() = WidgetProviderUtils()

    @Provides
    @Singleton
    fun provideGeoFenceDao(database: AppDatabase): GeoFenceDao = database.geoFenceDao()

    @Provides
    @Singleton
    fun provideGeoFenceController(context: Context): GeoFenceController = GeoFenceController(context)
}