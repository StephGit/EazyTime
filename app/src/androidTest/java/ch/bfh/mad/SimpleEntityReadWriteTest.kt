package ch.bfh.mad

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import ch.bfh.mad.eazytime.data.AppDatabase
import ch.bfh.mad.eazytime.data.entity.Project
import ch.bfh.mad.eazytime.data.entity.TimeSlot
import org.joda.time.LocalDateTime
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.ThreadLocalRandom

@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest {
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDatabase::class.java).build()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertProject() {
        val tmpProject = Project(randomLong(), randomUuid(), randomUuid(), randomUuid(), true,  false)
        db.projectDao().insertAll(mutableListOf(tmpProject))

        val projects = db.projectDao().getProjects()
        assert(projects.isNotEmpty())
    }

    @Test
    fun insertTimeSlot() {
        val tmpTimeSlot = TimeSlot(randomLong(), LocalDateTime.now().minusHours(2), LocalDateTime.now())
        db.timeSlotDao().insertAll(mutableListOf(tmpTimeSlot))

        val timeslots = db.timeSlotDao().getTimeSlots()
        assert(timeslots.isNotEmpty())
    }

    fun randomInt(): Int {
        return ThreadLocalRandom.current().nextInt(0, 1000 + 1)
    }

    fun randomLong(): Long {
        return randomInt().toLong()
    }

    fun randomUuid(): String {
        return java.util.UUID.randomUUID().toString()
    }
}