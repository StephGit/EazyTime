package ch.bfh.mad.eazytime.data.repo

import androidx.lifecycle.LiveData
import ch.bfh.mad.eazytime.data.dao.ProjectDao
import ch.bfh.mad.eazytime.data.entity.Project

class ProjectRepo(private val projectDao: ProjectDao) {
    val allProjects: LiveData<List<Project>> = projectDao.getProjectsLiveData()
}