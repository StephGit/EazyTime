package ch.bfh.mad.eazytime.projects.addProject

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.data.entity.Project
import ch.bfh.mad.eazytime.util.EazyTimeColorUtil
import javax.inject.Inject

class AddProjectViewModel @Inject constructor(): ViewModel() {

    val projectId = MutableLiveData<Long>()
    var projectName = MutableLiveData<String>()
    val shortCode = MutableLiveData<String>()
    val colorId = MutableLiveData<Int>()
    val onWidget = MutableLiveData<Boolean>()
    val defaultProject = MutableLiveData<Boolean>()
    val amountOnWidgetProjects = MutableLiveData<Int>()
    val initialOnWidget = MutableLiveData<Boolean>()


    fun selectProjectColor(selectedColor: Int){
        colorId.value = selectedColor
    }

    fun initializeWithProjectData(project: Project, eazyTimeColorUtil: EazyTimeColorUtil?){
        Log.i(TAG, "initializeWithProjectData: $project")
        projectId.postValue(project.id)
        projectName.postValue(project.name)
        shortCode.postValue(project.shortCode)
        onWidget.postValue(project.onWidget)
        defaultProject.postValue(project.isDefault)
        colorId.postValue(eazyTimeColorUtil?.getColorArrayId(project.color ?: ""))
    }
}