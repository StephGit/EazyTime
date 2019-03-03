package ch.bfh.mad.eazytime.util

import ch.bfh.mad.eazytime.data.dao.ProjectDao
import ch.bfh.mad.eazytime.data.entity.TimeSlot
import ch.bfh.mad.eazytime.data.entity.WorkDay
import ch.bfh.mad.eazytime.data.repo.TimeSlotRepo
import ch.bfh.mad.eazytime.data.repo.WorkDayRepo
import kotlinx.coroutines.*
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import kotlin.coroutines.CoroutineContext

class TimerService constructor(private val timeSlotRepo: TimeSlotRepo, private val projectDao: ProjectDao, private val workDayRepo: WorkDayRepo) {

    private var parentJob = Job()
    // By default all the coroutines launched in this scope should be using the Main dispatcher
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)


    private fun insertAll(timeSlots: List<TimeSlot>) = scope.launch(Dispatchers.IO) {
        timeSlotRepo.insertAll(timeSlots)
    }

    private fun update(timeSlot: TimeSlot) = scope.launch(Dispatchers.IO) {
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

    fun changeAndStartProject(projectId: Long) {
        stopCurrentTimeSlots()

        val newTs = TimeSlot()
        newTs.projectId = projectId
        newTs.startDate = LocalDateTime()
        newTs.workDayId = getWorkDayId()
        insertAll(listOf(newTs))
    }

    fun checkOut() {
        stopCurrentTimeSlots()
    }

    private fun stopCurrentTimeSlots() = runBlocking {
        val currentTimeSlots = async(Dispatchers.IO) {
            return@async timeSlotRepo.getCurrentTimeSlots()
        }.await()
        currentTimeSlots.forEach {
            it.endDate = LocalDateTime()
            update(it)
        }
    }

    private fun getWorkDayId(): Long = runBlocking {
        val curWorkDay = withContext(Dispatchers.IO) {
            workDayRepo.getWorkDayByDate(LocalDate())
        }

        return@runBlocking if (curWorkDay == null) {
            val newWorkDay = WorkDay()
            newWorkDay.date = LocalDate()
            withContext(Dispatchers.IO) {
                workDayRepo.insert(newWorkDay)
            }
        } else {
            curWorkDay.id
        }
    }
}