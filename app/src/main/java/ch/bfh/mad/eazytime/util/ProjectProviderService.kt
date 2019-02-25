package ch.bfh.mad.eazytime.util

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import ch.bfh.mad.eazytime.data.dao.ProjectDao
import ch.bfh.mad.eazytime.data.dao.TimeSlotDao
import ch.bfh.mad.eazytime.data.entity.Project
import ch.bfh.mad.eazytime.data.entity.TimeSlot
import ch.bfh.mad.eazytime.projects.ProjectListItem
import org.joda.time.LocalDateTime
import org.joda.time.Seconds

class ProjectProviderService(private val projectDao: ProjectDao, private val timeSlotDao: TimeSlotDao) {

    private var _projects: List<Project>? = listOf()
    private var _timeSlots: List<TimeSlot>? = listOf()


    fun getProjectListitems(): LiveData<List<ProjectListItem>> {
        val projectsLd: LiveData<List<Project>> = projectDao.getProjectsLiveData()
        val timeSlotsLd: LiveData<List<TimeSlot>> = timeSlotDao.getTimeSlotsLiveData()

        val result = MediatorLiveData<List<ProjectListItem>>()
        result.addSource(projectsLd) {projects ->
            _projects = projects
            result.value = mergeLiveData(projects, _timeSlots)
        }
        result.addSource(timeSlotsLd) {timeslots ->
            _timeSlots = timeslots
            result.value = mergeLiveData(_projects, timeslots)
        }

        return result
    }

    private fun mergeLiveData(projects: List<Project>?, timeslots: List<TimeSlot>?): List<ProjectListItem>? {
        return projects?.map {project ->
            var isActive = false
            val mappedTimeSlotSeconds = timeslots
                ?.filter { it.projectId == project.id && it.endDate != null }
                ?.map { Seconds.secondsBetween(it.startDate, it.endDate) }
                ?.fold(Seconds.seconds(0)) { acc, minutes -> acc.plus(minutes)}

            timeslots?.filter { it.projectId == project.id && it.endDate == null }?.firstOrNull()?.let {
                val currentSeconds = Seconds.secondsBetween(it.startDate, LocalDateTime())
                isActive = true
                mappedTimeSlotSeconds?.plus(currentSeconds)
            }

            ProjectListItem(project.name, project.shortCode, mappedTimeSlotSeconds?.seconds, project.color, project.isDefault, isActive)
        }

    }

}