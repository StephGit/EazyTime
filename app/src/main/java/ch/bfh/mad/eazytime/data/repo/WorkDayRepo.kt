package ch.bfh.mad.eazytime.data.repo

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import ch.bfh.mad.eazytime.data.dao.WorkDayDao
import ch.bfh.mad.eazytime.data.entity.WorkDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.joda.time.LocalDate

class WorkDayRepo(private val workDayDao: WorkDayDao) {

    @WorkerThread
    suspend fun getWorkDayByDate(localDate: LocalDate): WorkDay? = withContext(Dispatchers.IO) {
        workDayDao.getWorkDayByDate(localDate)
    }

    @WorkerThread
    suspend fun insert(newWorkDay: WorkDay): Long = withContext(Dispatchers.IO) {
        workDayDao.insert(newWorkDay)
    }

    @WorkerThread
    suspend fun getWorkDays(): LiveData<List<WorkDay>> = withContext(Dispatchers.IO){
        workDayDao.getWorkDays()
    }

    @WorkerThread
    suspend fun getWorkDayAndTimeSlotsById(id: Long): List<WorkDayDao.WorkDayTimeSlotProject> = withContext(Dispatchers.IO){
        workDayDao.getWorkDayTimeSlotProject(id)
    }
}