package ch.bfh.mad.eazytime.projects

import ch.bfh.mad.eazytime.data.dao.ProjectDao
import ch.bfh.mad.eazytime.data.entity.Project
import javax.inject.Inject

class FakeProjectProviderService @Inject constructor(private val projectDao: ProjectDao) {

    fun getFakeProjectList(): List<ProjectListItem> {
        val projects = projectDao.getProjects()
        val projectListItems = mutableListOf<ProjectListItem>()
        projects.forEach { projectListItems.add(createProjectListItem(it)) }
        return projectListItems
    }

    private fun createProjectListItem(project: Project, currentTime: String = ""): ProjectListItem {
        val active = project.onWidget == true
        return ProjectListItem(project.id, project.name, project.shortCode, currentTime, project.color, project.isDefault, active)
    }
}