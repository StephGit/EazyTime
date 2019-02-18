package ch.bfh.mad.eazytime.util

import ch.bfh.mad.eazytime.data.dao.ProjectDao
import ch.bfh.mad.eazytime.data.dao.TimeSlotDao
import ch.bfh.mad.eazytime.data.dao.WorkDayDao
import ch.bfh.mad.eazytime.data.entity.Project
import ch.bfh.mad.eazytime.data.entity.TimeSlot
import ch.bfh.mad.eazytime.data.entity.WorkDay
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime

class TimerService constructor(private val timeSlotDao: TimeSlotDao, private val projectDao: ProjectDao, private val workDayDao: WorkDayDao) {

    fun checkInDefaultProject() {
        // if already checked-in, do nothing
        if (timeSlotDao.getCurrentTimeSlots().size == 1) {
            return
        }

        val defaultProject = projectDao.getDefaultProject()
        val ts = TimeSlot()
        ts.projectId = defaultProject.id
        ts.startDate = LocalDateTime()
        ts.workDayId = getWorkDayId()
        timeSlotDao.insertAll(listOf(ts))
    }

    fun changeAndStartProject(project: Project) {
        stopCurrentTimeSlots()

        val newTs = TimeSlot()
        newTs.projectId = project.id
        newTs.startDate = LocalDateTime()
        newTs.workDayId = getWorkDayId()
        timeSlotDao.insertAll(listOf(newTs))
    }

    fun checkOut() {
        stopCurrentTimeSlots()
    }

    private fun stopCurrentTimeSlots() {
        timeSlotDao.getCurrentTimeSlots().forEach {
            it.endDate = LocalDateTime()
            timeSlotDao.update(it)
        }
    }

    private fun getWorkDayId(): Long {
        val curWorkDay = workDayDao.getWorkDayByDate(LocalDate())
        return if (curWorkDay == null) {
            val newWorkDay = WorkDay()
            newWorkDay.date = LocalDate()
            workDayDao.insert(newWorkDay)
        } else {
            curWorkDay.id
        }
    }
}