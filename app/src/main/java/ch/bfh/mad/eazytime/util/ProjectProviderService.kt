package ch.bfh.mad.eazytime.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import ch.bfh.mad.eazytime.data.entity.Project
import ch.bfh.mad.eazytime.data.entity.TimeSlot
import ch.bfh.mad.eazytime.data.repo.ProjectRepo
import ch.bfh.mad.eazytime.data.repo.TimeSlotRepo
import ch.bfh.mad.eazytime.projects.ProjectListItem
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.Seconds

class ProjectProviderService(projectRepo: ProjectRepo, timeSlotRepo: TimeSlotRepo) {

    private var _projects: List<Project>? = emptyList()
    private var _timeSlots: List<TimeSlot>? = emptyList()

    private val projectItemList: MediatorLiveData<List<ProjectListItem>>

    init {
        val projectsLd: LiveData<List<Project>> = projectRepo.allProjects
        val timeSlotsLd: LiveData<List<TimeSlot>> = timeSlotRepo.allTimeSlots

        projectItemList = MediatorLiveData()
        projectItemList.addSource(projectsLd) {projects ->
            _projects = projects
            projectItemList.value = mergeLiveData(projects, _timeSlots)
        }
        projectItemList.addSource(timeSlotsLd) {timeslots ->
            _timeSlots = timeslots
            projectItemList.value = mergeLiveData(_projects, timeslots)
        }
    }


    fun getProjectListitems(): LiveData<List<ProjectListItem>> {
        return projectItemList
    }

    private fun mergeLiveData(projects: List<Project>?, timeslots: List<TimeSlot>?): List<ProjectListItem>? {
        // need to be filtered  here because LiveData-Queries with parameter are not allowed
        val filteredTimeSlots = timeslots?.filter { timeSlot -> isFromToday(timeSlot) }

        return projects?.map {project ->
            var isActive = false
            var mappedTimeSlotSeconds = filteredTimeSlots
                ?.filter { it.projectId == project.id && it.endDate != null }
                ?.map { Seconds.secondsBetween(it.startDate, it.endDate) }
                ?.fold(Seconds.seconds(0)) { acc, minutes -> acc.plus(minutes)}

            val activeTimeslot = filteredTimeSlots?.filter { it.projectId == project.id && it.endDate == null }?.firstOrNull()?.also { isActive = true }

            ProjectListItem(project.id, project.name, project.shortCode, mappedTimeSlotSeconds?.seconds, activeTimeslot?.startDate, project.color, project.isDefault, project.onWidget, isActive, project.isDeleted)
        }

    }

    private fun isFromToday(ts: TimeSlot) = ts.startDate?.toDate()?.time ?: 0 >= LocalDate().toDate().time

}