package ch.bfh.mad.eazytime.projects

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import ch.bfh.mad.eazytime.util.ProjectProviderService

class ProjectListViewModel(projectProviderService: ProjectProviderService): ViewModel(){

    val projects: LiveData<List<ProjectListItem>> = projectProviderService.getProjectListitems()

}