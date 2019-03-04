package ch.bfh.mad.eazytime.data.repo

import androidx.annotation.WorkerThread
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
}