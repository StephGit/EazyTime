package ch.bfh.mad

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import ch.bfh.mad.eazytime.data.AppDatabase
import ch.bfh.mad.eazytime.data.entity.Project
import ch.bfh.mad.eazytime.util.TimerService
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TimerServiceTest {
    private lateinit var db: AppDatabase
    private lateinit var ts: TimerService


    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDatabase::class.java).build()
        ts = TimerService(db.timeSlotDao(), db.projectDao())

        val p1 = Project()
        p1.isDefault = true
        p1.name = "default-project"
        p1.shortCode = "def"
        p1.color = "fuchsia"

        val p2 = Project()
        p2.isDefault = false
        p2.name = "other-project"
        p2.shortCode = "moo"
        p2.color = "ja"

        db.projectDao().insertAll(listOf(p1, p2))
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun checkinDefaultProject() {
        val nullTs = db.timeSlotDao().getCurrentTimeSlot()
        assert(nullTs == null)
        val defaultProject = db.projectDao().getDefaultProject()
        ts.checkInDefaultProject()

        val newTs = db.timeSlotDao().getCurrentTimeSlot()
        assert(defaultProject.id == newTs?.projectId)
    }

    @Test
    fun changeProject() {
        ts.checkInDefaultProject()
        val oldTs = db.timeSlotDao().getCurrentTimeSlot()
        val otherProject = db.projectDao().getProjects().find { p -> p.shortCode == "moo" }
        ts.changeAndStartProject(otherProject!!)
        val newTs = db.timeSlotDao().getCurrentTimeSlot()
        assert(oldTs != newTs)
    }

    @Test
    fun checkout() {
        ts.checkInDefaultProject()
        Thread.sleep(500)
        ts.checkOut()
        val slot = db.timeSlotDao().getTimeSlots().first()
        assert(slot.startDate!!.isBefore(slot.endDate))
    }

}