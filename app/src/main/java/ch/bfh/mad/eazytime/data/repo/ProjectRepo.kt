package ch.bfh.mad.eazytime.data.repo

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import ch.bfh.mad.eazytime.data.dao.ProjectDao
import ch.bfh.mad.eazytime.data.entity.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProjectRepo(private val projectDao: ProjectDao) {
    val allProjects: LiveData<List<Project>> = projectDao.getProjectsLiveData()

    @WorkerThread
    suspend fun deleteProject(id: Long) = withContext(Dispatchers.IO) {
        val project = getProjectById(id)
        project?.let {
            it.isDeleted = true
            update(it)
        }
    }

    @WorkerThread
    suspend fun rollbackDeleteProject(id: Long) = withContext(Dispatchers.IO) {
        val project = getProjectById(id)
        project?.let {
            it.isDeleted = false
            update(it)
        }
    }

    @WorkerThread
    suspend fun getProjectById(id: Long): Project? = withContext(Dispatchers.IO){
        projectDao.getProjectById(id)
    }

    @WorkerThread
    suspend fun getProjects(): List<Project> = withContext(Dispatchers.IO){
        projectDao.getProjects()
    }

    @WorkerThread
    suspend fun updateAll(projects: List<Project>) = withContext(Dispatchers.IO) {
        projectDao.updateAll(projects)
    }

    @WorkerThread
    suspend fun update(project: Project) = withContext(Dispatchers.IO) {
        projectDao.update(project)
    }

    @WorkerThread
    suspend fun insert(project: Project) = withContext(Dispatchers.IO) {
        projectDao.insert(project)
    }

    @WorkerThread
    suspend fun getAmountOfProjectsOnWidget(): Int = withContext(Dispatchers.IO) {
        projectDao.getAmountOfProjectsOnWidget()
    }
}