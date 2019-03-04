package ch.bfh.mad.projects.addProject

import ch.bfh.mad.eazytime.data.dao.ProjectDao
import ch.bfh.mad.eazytime.data.entity.Project
import ch.bfh.mad.eazytime.projects.addProject.ProjectSaveOrUpdateService
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as Is

class ProjectSaveOrUpdateServiceTest {

    private lateinit var service: ProjectSaveOrUpdateService

    private val projectDaoMock = mock<ProjectDao>()

    @Before
    fun setup() {
        service = ProjectSaveOrUpdateService(projectDaoMock)
    }

    @Test
    fun saveOrUpdateProject_New_Project() {
        service.saveOrUpdateProject(Project(0, "name", "shortCode", "color", onWidget = false, isDefault = false))
        verify(projectDaoMock).insert(Project(0, "name", "shortCode", "color", onWidget = false, isDefault = false))
        verifyNoMoreInteractions(projectDaoMock)
    }

    @Test
    fun saveOrUpdateProject_existing_Project() {
        service.saveOrUpdateProject(Project(4, "name", "shortCode", "color", onWidget = false, isDefault = false))
        verify(projectDaoMock).update(Project(4, "name", "shortCode", "color", onWidget = false, isDefault = false))
        verifyNoMoreInteractions(projectDaoMock)
    }

    @Test
    fun saveOrUpdateProject_new_Project_isDefault() {
        val p1 = Project(1, "p1", "s1", "color", onWidget = false, isDefault = true)
        val p2 = Project(2, "p2", "s2", "color", onWidget = false, isDefault = false)
        val existingProjects = arrayListOf(p1, p2)
        val newProject = Project(0, "name", "shortCode", "color", onWidget = false, isDefault = true)
        whenever(projectDaoMock.getProjects()).thenReturn(existingProjects)

        service.saveOrUpdateProject(newProject)

        verify(projectDaoMock).getProjects()
        verify(projectDaoMock).updateAll(existingProjects)
        verify(projectDaoMock).insert(newProject)
        verifyNoMoreInteractions(projectDaoMock)
        assertThat(p1.isDefault, Is(false))
        assertThat(p2.isDefault, Is(false))
    }

    @Test
    fun saveOrUpdateProject_update_existing_Project_isDefault() {
        val p1 = Project(1, "p1", "s1", "color", onWidget = false, isDefault = true)
        val p2 = Project(2, "p2", "s2", "color", onWidget = false, isDefault = false)
        val p3 = Project(3, "p3", "s3", "color", onWidget = false, isDefault = false)
        val existingProjects = arrayListOf(p1, p2, p3)
        val updatedProject = Project(3, "p3", "s3", "color", onWidget = false, isDefault = true)
        whenever(projectDaoMock.getProjects()).thenReturn(existingProjects)

        service.saveOrUpdateProject(updatedProject)

        verify(projectDaoMock).getProjects()
        verify(projectDaoMock).updateAll(existingProjects)
        verify(projectDaoMock).update(updatedProject)
        verifyNoMoreInteractions(projectDaoMock)
        assertThat(p1.isDefault, Is(false))
        assertThat(p2.isDefault, Is(false))
    }
}