package ch.bfh.mad.eazytime.projects

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import ch.bfh.mad.eazytime.projects.addProject.AddProjectViewModel
import java.lang.Exception

class ProjectModelFactory(var fakeProjectProviderService: FakeProjectProviderService, var fakeProjectRepo: FakeProjectRepo): ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when(modelClass){
            ProjectListViewModel::class.java -> ProjectListViewModel(fakeProjectProviderService)
            AddProjectViewModel::class.java -> AddProjectViewModel(fakeProjectRepo)
            else -> throw Exception("Unknown Project ViewModel")
        } as T
    }
}