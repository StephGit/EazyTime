package ch.bfh.mad.eazytime.di

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import ch.bfh.mad.eazytime.data.AppDatabase
import ch.bfh.mad.eazytime.data.dao.ProjectDao
import ch.bfh.mad.eazytime.data.dao.TimeSlotDao
import ch.bfh.mad.eazytime.data.dao.WorkDayDao
import ch.bfh.mad.eazytime.data.repo.ProjectRepo
import ch.bfh.mad.eazytime.data.repo.TimeSlotRepo
import ch.bfh.mad.eazytime.data.repo.WorkDayRepo
import ch.bfh.mad.eazytime.projects.addProject.ProjectSaveOrUpdateService
import ch.bfh.mad.eazytime.util.EazyTimeColorUtil
import ch.bfh.mad.eazytime.util.ProjectProviderService
import ch.bfh.mad.eazytime.util.TimerService
import ch.bfh.mad.eazytime.util.WidgetProviderUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application

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
}