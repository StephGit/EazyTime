package ch.bfh.mad.eazytime.util

import ch.bfh.mad.eazytime.data.dao.ProjectDao
import ch.bfh.mad.eazytime.data.entity.TimeSlot
import ch.bfh.mad.eazytime.data.entity.WorkDay
import ch.bfh.mad.eazytime.data.repo.TimeSlotRepo
import ch.bfh.mad.eazytime.data.repo.WorkDayRepo
import kotlinx.coroutines.runBlocking
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.Period

class TimerService constructor(private val timeSlotRepo: TimeSlotRepo, private val projectDao: ProjectDao, private val workDayRepo: WorkDayRepo) {

    private fun insertAll(timeSlots: List<TimeSlot>) = runBlocking {
        timeSlotRepo.insertAll(timeSlots)
    }

    private fun update(timeSlot: TimeSlot) = runBlocking {
        timeSlotRepo.update(timeSlot)
    }

    fun checkInDefaultProject() {
        // if already checked-in, do nothing
        if (timeSlotRepo.allTimeSlots.value?.size == 1) {
            return
        }

        val defaultProject = projectDao.getDefaultProject()
        val ts = TimeSlot()
        ts.projectId = defaultProject.id
        ts.startDate = LocalDateTime()
        ts.workDayId = getWorkDayId()
        insertAll(listOf(ts))
    }

    fun changeAndStartProject(projectId: Long) = runBlocking {
        val onlyStop = timeSlotRepo.getCurrentTimeSlots().any { timeSlot -> timeSlot.projectId == projectId }

        stopCurrentTimeSlots()
        if (!onlyStop) {
            val newTs = TimeSlot()
            newTs.projectId = projectId
            newTs.startDate = LocalDateTime()
            newTs.workDayId = getWorkDayId()

            timeSlotRepo.insertAll(listOf(newTs))
        }
    }

    fun checkOut() {
        stopCurrentTimeSlots()
    }

    private fun stopCurrentTimeSlots() = runBlocking {
        val currentTimeSlots =  timeSlotRepo.getCurrentTimeSlots()
        currentTimeSlots.forEach {
            it.endDate = LocalDateTime()
            update(it)
        }
        calculateTotalWorkHours()
    }

    private fun calculateTotalWorkHours() = runBlocking {
        val curWorkDay = workDayRepo.getWorkDayByDate(LocalDate())
        curWorkDay?.let {
            val workDayAndTimeSlots = workDayRepo.getWorkDayAndTimeSlotsById(it.id)

            val totalMinutes = workDayAndTimeSlots?.timeslots?.map { ts ->
                Period(ts.startDate, ts.endDate).minutes
            }?.sum()

            totalMinutes?.let {
                curWorkDay.totalWorkHours = 1F * totalMinutes / 60
                workDayRepo.update(curWorkDay)
            }
        }
    }

    private fun getWorkDayId(): Long = runBlocking {
        val curWorkDay = workDayRepo.getWorkDayByDate(LocalDate())

        return@runBlocking if (curWorkDay == null) {
            val newWorkDay = WorkDay()
            newWorkDay.date = LocalDate()
            workDayRepo.insert(newWorkDay)
        } else {
            curWorkDay.id
        }
    }
}