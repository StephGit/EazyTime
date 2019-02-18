package ch.bfh.mad.eazytime.projects.addProject

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.projects.FakeProjectRepo

class AddProjectViewModel(private var fakeProjectRepo: FakeProjectRepo) :ViewModel() {

    var projectName = MutableLiveData<String>()
    val shortCode = MutableLiveData<String>()
    val colorId = MutableLiveData<Int>()
    val onWidget = MutableLiveData<Boolean>()
    val defaultProject = MutableLiveData<Boolean>()

    fun onDeleteButtonClick() {
        logTheShit()
    }

    fun onSaveButtonClick() {
        logTheShit()
        if (projectName.value == null){
            projectName.value = ""
        } else {
            createAndSaveProject()
        }
    }

    private fun createAndSaveProject() {
        fakeProjectRepo.addProject(projectName.value?: "ProjectName", shortCode.value?: "SCD", "ffd43b")
    }

    fun selectProjectColor(selectedColor: Int){
        colorId.value = selectedColor
    }

    private fun logTheShit(){
        Log.i(TAG, "projectName: ${projectName.value}")
        Log.i(TAG, "shortCode: ${shortCode.value}")
        Log.i(TAG, "colorItemPosition: ${colorId.value}")
        Log.i(TAG, "onWidget: ${onWidget.value}")
        Log.i(TAG, "defaultProject: ${defaultProject.value}")
    }
}