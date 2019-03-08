package ch.bfh.mad

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import ch.bfh.mad.eazytime.data.AppDatabase
import ch.bfh.mad.eazytime.data.entity.Project
import ch.bfh.mad.eazytime.data.repo.TimeSlotRepo
import ch.bfh.mad.eazytime.data.repo.WorkDayRepo
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
        ts = TimerService(TimeSlotRepo(db.timeSlotDao()), db.projectDao(), WorkDayRepo( db.workDayDao()))

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
    fun checkInDefaultProject() {
        val emptyTs = db.timeSlotDao().getCurrentTimeSlots()
        assert(emptyTs.isEmpty())
        val defaultProject = db.projectDao().getDefaultProject()
        ts.checkInDefaultProject()

        val newTs = db.timeSlotDao().getCurrentTimeSlots().first()
        assert(defaultProject.id == newTs.projectId)
    }

    @Test
    fun changeProject() {
        ts.checkInDefaultProject()
        val oldTs = db.timeSlotDao().getCurrentTimeSlots().first()
        val otherProject = db.projectDao().getProjects().find { p -> p.shortCode == "moo" }
        ts.changeAndStartProject(otherProject!!.id)
        val newTs = db.timeSlotDao().getCurrentTimeSlots().first()
        assert(oldTs != newTs)
    }

    @Test
    fun checkout() {
        ts.checkInDefaultProject()
        Thread.sleep(500)
        ts.checkOut()
        val slot = db.timeSlotDao().getTimeSlots().value?.first()
        assert(slot?.startDate?.isBefore(slot.endDate) == true)
    }

}