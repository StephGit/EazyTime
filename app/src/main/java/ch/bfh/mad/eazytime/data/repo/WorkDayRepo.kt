package ch.bfh.mad.eazytime.data.repo

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread
import ch.bfh.mad.eazytime.data.dao.WorkDayDao
import ch.bfh.mad.eazytime.data.entity.WorkDay
import org.joda.time.LocalDate

@Suppress("RedundantSuspendModifier")
class WorkDayRepo(private val workDayDao: WorkDayDao) {

    @WorkerThread
    suspend fun getWorkDayByDate(localDate: LocalDate): WorkDay? {
        return workDayDao.getWorkDayByDate(localDate)
    }

    @WorkerThread
    suspend fun insert(newWorkDay: WorkDay): Long {
        return workDayDao.insert(newWorkDay)
    }

    @WorkerThread
    suspend fun getWorkDays(): LiveData<List<WorkDay>> {
        return workDayDao.getWorkDays()
    }
}