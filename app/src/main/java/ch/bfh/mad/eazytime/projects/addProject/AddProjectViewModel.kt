package ch.bfh.mad.eazytime.projects.addProject

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import ch.bfh.mad.eazytime.TAG

class AddProjectViewModel :ViewModel() {

    var projectName = MutableLiveData<String>()
    val shortCode = MutableLiveData<String>()
    val colorItemPosition = MutableLiveData<Int>()
    val onWidget = MutableLiveData<Boolean>()
    val defaultProject = MutableLiveData<Boolean>()

    fun onDeleteButtonClick() {
        Log.i(TAG, "AddProjectViewModel.onDeleteButtonClick")
        logTheShit()
    }

    fun onSaveButtonClick() {
        Log.i(TAG, "AddProjectViewModel.onSaveButtonClick")
        logTheShit()
    }

    private fun logTheShit(){
        Log.i(TAG, "projectName: ${projectName.value}")
        Log.i(TAG, "shortCode: ${shortCode.value}")
        Log.i(TAG, "colorItemPosition: ${colorItemPosition.value}")
        Log.i(TAG, "onWidget: ${onWidget.value}")
        Log.i(TAG, "defaultProject: ${defaultProject.value}")
    }
}