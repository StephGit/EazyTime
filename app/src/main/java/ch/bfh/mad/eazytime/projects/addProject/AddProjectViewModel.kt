package ch.bfh.mad.eazytime.projects.addProject

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.data.dao.ProjectDao
import ch.bfh.mad.eazytime.data.entity.Project

class AddProjectViewModel(private var projectDao: ProjectDao) :ViewModel() {

    val projectId = MutableLiveData<Long>()
    var projectName = MutableLiveData<String>()
    val shortCode = MutableLiveData<String>()
    val colorId = MutableLiveData<Int>()
    val onWidget = MutableLiveData<Boolean>()
    val defaultProject = MutableLiveData<Boolean>()


    fun selectProjectColor(selectedColor: Int){
        colorId.value = selectedColor
    }

    fun initializeWithProjectData(project: Project){
        Log.i(TAG, "initializeWithProjectData: $project")
        projectId.postValue(project.id)
        projectName.postValue(project.name)
        shortCode.postValue(project.shortCode)
        onWidget.postValue(project.onWidget)
        defaultProject.postValue(project.isDefault)
    }
}