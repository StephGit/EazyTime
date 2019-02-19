package ch.bfh.mad.eazytime.projects

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.AsyncTask

class ProjectListViewModel(private var fakeProjectProviderService: FakeProjectProviderService): ViewModel(){

    val projects: MutableLiveData<List<ProjectListItem>> = MutableLiveData()

    fun refreshListItems(){
        val asyncTask = GetProjectListItemsAsyncTask(projects)
        asyncTask.fakeProjectProviderService = fakeProjectProviderService
        asyncTask.execute()
    }


    private class GetProjectListItemsAsyncTask internal constructor(private val projects: MutableLiveData<List<ProjectListItem>>) : AsyncTask<Void, Void, Void>() {
        var fakeProjectProviderService: FakeProjectProviderService? = null
        override fun doInBackground(vararg params: Void?): Void? {
            fakeProjectProviderService?.let { service ->
                projects.postValue(service.getFakeProjectList())
            }
            return null
        }
    }
}