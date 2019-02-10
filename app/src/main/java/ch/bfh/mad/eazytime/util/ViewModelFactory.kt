package ch.bfh.mad.eazytime.util

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
<<<<<<< HEAD
=======
import ch.bfh.mad.eazytime.geofence.GeoFenceViewModel
>>>>>>> ad19f6c... added geofence components

//class ViewModelFactory @Inject constructor(
//    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelProvider.Factory {
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        var creator: Provider<ViewModel>? = creators[modelClass]
//        if (creator == null) {
//            for ((key, value) in creators) {
//                if (modelClass.isAssignableFrom(key)) {
//                    creator = value
//                    break
//                }
//            }
//        }
//        if (creator == null) throw IllegalArgumentException("unknown model class " + modelClass)
//        try {
//            return creator.get() as T
//        } catch (e: Exception) {
//            throw RuntimeException(e)
//        }
//    }
//}
//


// TODO better pass dependencies over map not inject here


class ViewModelFactory: ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
<<<<<<< HEAD
//            GeoFenceViewModel::class.java -> GeoFenceViewModel()
=======
            GeoFenceViewModel::class.java -> GeoFenceViewModel()
>>>>>>> ad19f6c... added geofence components
            else -> throw IllegalStateException("ViewModel not found")
        } as T
    }
}