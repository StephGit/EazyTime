package ch.bfh.mad.eazytime.projects

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import ch.bfh.mad.eazytime.TAG

class ProjectListViewModel(private var fakeProjectProviderService: FakeProjectProviderService): ViewModel(){

    val projects: MutableLiveData<List<ProjectListItem>> = MutableLiveData()

    fun refreshListItems(){
        Log.i(TAG, "ProjectListViewModel.getProjectListItems")
        projects.value = fakeProjectProviderService.getFakeProjectList()
    }
}