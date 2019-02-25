package ch.bfh.mad.eazytime.projects

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import ch.bfh.mad.eazytime.data.dao.ProjectDao
import ch.bfh.mad.eazytime.projects.addProject.AddProjectViewModel
import ch.bfh.mad.eazytime.util.ProjectProviderService
import java.lang.Exception

class ProjectModelFactory(var projectProviderService: ProjectProviderService, var projectDao: ProjectDao): ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when(modelClass){
            ProjectListViewModel::class.java -> ProjectListViewModel(projectProviderService)
            AddProjectViewModel::class.java -> AddProjectViewModel(projectDao)
            else -> throw Exception("Unknown Project ViewModel")
        } as T
    }
}