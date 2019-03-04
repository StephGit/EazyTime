package ch.bfh.mad.eazytime.projects.addProject

import android.util.Log
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.data.dao.ProjectDao
import ch.bfh.mad.eazytime.data.entity.Project

class ProjectSaveOrUpdateService constructor(val projectDao: ProjectDao) {

    fun saveOrUpdateProject(project: Project) {
        if (project.isDefault == true){
            markExistingProjectsASNotDefault()
        }
        if (project.id > 0){
            Log.i(TAG,"Update Existing Project: $project")
         projectDao.update(project)
        }else {
            Log.i(TAG,"Save new Project: $project")
            projectDao.insert(project)
        }
    }

    private fun markExistingProjectsASNotDefault() {
        val projects = projectDao.getProjects()
        for (project in projects) {
            project.isDefault = false
        }
        projectDao.updateAll(projects)
    }
}