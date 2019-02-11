package ch.bfh.mad.eazytime.projects

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import java.lang.Exception

class ProjectModelFactory(var fakeProjectProviderService: FakeProjectProviderService): ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when(modelClass){
            ProjectListViewModel::class.java -> ProjectListViewModel(fakeProjectProviderService)
            else -> throw Exception("Unknown Project ViewModel")
        } as T
    }
}