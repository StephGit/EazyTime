package ch.bfh.mad.eazytime.di

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import ch.bfh.mad.eazytime.data.AppDatabase
import ch.bfh.mad.eazytime.data.dao.ProjectDao
import ch.bfh.mad.eazytime.data.dao.TimeSlotDao
import ch.bfh.mad.eazytime.util.TimerService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application) : Context = application

    @Provides
    fun provideAppDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "EazyTimeDB").build()

    @Provides
    fun provideTimeSlotDao(database: AppDatabase) = database.timeSlotDao()

    @Provides
    fun provideProjectDao(database: AppDatabase) = database.projectDao()

    @Provides
    fun provideTimerService(timeSlotDao: TimeSlotDao, projectDao: ProjectDao) = TimerService(timeSlotDao, projectDao)


}