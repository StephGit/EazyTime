package ch.bfh.mad.eazytime.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ch.bfh.mad.eazytime.geofence.GeoFenceViewModel
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
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}