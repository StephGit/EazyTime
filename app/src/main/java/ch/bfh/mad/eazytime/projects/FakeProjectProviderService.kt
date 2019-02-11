package ch.bfh.mad.eazytime.projects

import ch.bfh.mad.eazytime.data.entity.Project
import javax.inject.Inject

class FakeProjectProviderService @Inject constructor(private val fakeProjectRepo: FakeProjectRepo) {

    fun getFakeProjectList(): List<ProjectListItem> {
        val projects = fakeProjectRepo.getProjects()
        val projectListItems = mutableListOf<ProjectListItem>()
        projects.forEach { project ->
            when (project.id.toInt()) {
                1 -> projectListItems.add(createProjectListItem(project, "04:45:78"))
                3 -> projectListItems.add(createProjectListItem(project, "03.23:45"))
                else -> projectListItems.add(createProjectListItem(project))
            }
        }
        return projectListItems
    }

    private fun createProjectListItem(project: Project, currentTime: String = ""): ProjectListItem {
        return ProjectListItem(project.name, project.shortCode, currentTime, project.color)
    }
}