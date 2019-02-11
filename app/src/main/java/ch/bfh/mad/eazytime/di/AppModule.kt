package ch.bfh.mad.eazytime.di

import android.app.Application
import android.content.Context
import ch.bfh.mad.eazytime.projects.FakeProjectProviderService
import ch.bfh.mad.eazytime.projects.FakeProjectRepo
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
    fun providesFakeProjectRepo(): FakeProjectRepo {
        return FakeProjectRepo()
    }

    @Provides
    @Singleton
    fun providesFakeProjectProviderService(fakeProjectRepo: FakeProjectRepo): FakeProjectProviderService {
        return FakeProjectProviderService(fakeProjectRepo)
    }

}