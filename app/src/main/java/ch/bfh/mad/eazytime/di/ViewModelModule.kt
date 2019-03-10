package ch.bfh.mad.eazytime.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ch.bfh.mad.eazytime.calendar.CalendarListViewModel
import ch.bfh.mad.eazytime.calendar.detail.CalendarDetailViewModel
import ch.bfh.mad.eazytime.geofence.GeoFenceViewModel
import ch.bfh.mad.eazytime.projects.ProjectListViewModel
import ch.bfh.mad.eazytime.projects.addProject.AddProjectViewModel
import ch.bfh.mad.eazytime.util.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(GeoFenceViewModel::class)
    internal abstract fun bindGeoFenceViewModel(geoFenceViewModel: GeoFenceViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CalendarListViewModel::class)
    internal abstract fun bindCalendarListViewModel(calendarListViewModel: CalendarListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CalendarDetailViewModel::class)
    internal abstract fun bindCalendarDetailViewModel(calendarDetailViewModel: CalendarDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProjectListViewModel::class)
    internal abstract fun bindProjectListViewModel(projectListViewModel: ProjectListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddProjectViewModel::class)
    internal abstract fun bindAddProjectViewModel(addProjectViewModel: AddProjectViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}