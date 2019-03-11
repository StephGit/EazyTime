package ch.bfh.mad.eazytime.projects

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ch.bfh.mad.eazytime.util.ProjectProviderService
import javax.inject.Inject

class ProjectListViewModel @Inject constructor(val projectProviderService: ProjectProviderService): ViewModel(){

    val projects: LiveData<List<ProjectListItem>> = projectProviderService.getProjectListitems()

}