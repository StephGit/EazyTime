package ch.bfh.mad.eazytime.projects.addProject

import android.util.Log
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.data.entity.Project
import ch.bfh.mad.eazytime.data.repo.ProjectRepo
import kotlinx.coroutines.runBlocking

class ProjectSaveOrUpdateService constructor(val projectRepo: ProjectRepo) {

    fun saveOrUpdateProject(project: Project): Unit = runBlocking {
        if (project.isDefault == true){
            markExistingProjectsASNotDefault()
        }
        if (project.id > 0){
            Log.i(TAG,"Update Existing Project: $project")
            projectRepo.update(project)
        }else {
            Log.i(TAG,"Save new Project: $project")
            projectRepo.insert(project)
        }
    }

    private fun markExistingProjectsASNotDefault(): Unit = runBlocking {
        val projects = projectRepo.getProjects()
        for (project in projects) {
            project.isDefault = false
        }
        projectRepo.updateAll(projects)
    }
}