package ch.bfh.mad

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import ch.bfh.mad.eazytime.data.AppDatabase
import ch.bfh.mad.eazytime.data.entity.Project
import ch.bfh.mad.eazytime.data.entity.TimeSlot
import ch.bfh.mad.eazytime.util.ProjectProviderService
import ch.bfh.mad.eazytime.util.TimerService
import org.joda.time.LocalDateTime
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProjectProviderServiceTest {
    private lateinit var db: AppDatabase
    private lateinit var pps: ProjectProviderService
    private lateinit var ts: TimerService


    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDatabase::class.java).build()
        pps = ProjectProviderService(db.projectDao(), db.timeSlotDao())
        ts = TimerService(db.timeSlotDao(), db.projectDao(), db.workDayDao())

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


        val defProjectId = db.projectDao().getProjects().find { it.shortCode == "def" }?.id
        val ts1 = TimeSlot()
        ts1.projectId = defProjectId
        ts1.startDate = LocalDateTime().minusMinutes(90)
        ts1.endDate = LocalDateTime()

        db.timeSlotDao().insertAll(listOf(ts1))

        pps.getProjectListitems().observeForever {
            print(it)
        }
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun checkinDefaultProject() {
        ts.checkInDefaultProject()
        Thread.sleep(30000)
    }

}