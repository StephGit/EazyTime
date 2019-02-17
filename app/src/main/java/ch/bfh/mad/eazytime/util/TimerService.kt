package ch.bfh.mad.eazytime.util

import ch.bfh.mad.eazytime.data.dao.ProjectDao
import ch.bfh.mad.eazytime.data.dao.TimeSlotDao
import ch.bfh.mad.eazytime.data.entity.Project
import ch.bfh.mad.eazytime.data.entity.TimeSlot
import org.joda.time.LocalDateTime

class TimerService constructor(private val timeSlotDao: TimeSlotDao, private val projectDao: ProjectDao) {

    fun checkInDefaultProject() {
        //todo: check currently not checked-in already. what should happen if already checked-in? tbd...
        val defaultProject = projectDao.getDefaultProject()
        val ts = TimeSlot()
        ts.projectId = defaultProject.id
        ts.startDate = LocalDateTime()
        timeSlotDao.insertAll(listOf(ts))
    }

    fun changeAndStartProject(project: Project) {
        timeSlotDao.getCurrentTimeSlot()?.let {
            it.endDate = LocalDateTime()
            timeSlotDao.update(it)
        }

        val newTs = TimeSlot()
        newTs.projectId = project.id
        newTs.startDate = LocalDateTime()
        timeSlotDao.insertAll(listOf(newTs))
    }

    fun checkOut() {
        timeSlotDao.getCurrentTimeSlot()?.let {
            it.endDate = LocalDateTime()
            timeSlotDao.update(it)
        }
    }
}