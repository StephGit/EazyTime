package ch.bfh.mad

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import ch.bfh.mad.eazytime.data.AppDatabase
import ch.bfh.mad.eazytime.data.entity.GeoFence
import ch.bfh.mad.eazytime.data.entity.Project
import ch.bfh.mad.eazytime.data.entity.TimeSlot
import org.joda.time.LocalDateTime
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.ThreadLocalRandom


@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest : LifecycleOwner {
    // mock lifecycle
    override fun getLifecycle(): Lifecycle = LifecycleRegistry(this)

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
    fun getActiveGeoFence() {
        assert(db.geoFenceDao().getActiveGeoFences().isEmpty())

        val tmpGeoFence = GeoFence(randomUuid(), true, randomUuid(), randomDouble(), randomDouble(), randomDouble())
        val tmpGeoFence2 = GeoFence(randomUuid(), false, randomUuid(), randomDouble(), randomDouble(), randomDouble())
        val entityId = db.geoFenceDao().insert(tmpGeoFence)
        val entityId2 = db.geoFenceDao().insert(tmpGeoFence2)
        assert(entityId >= 0)
        assert(entityId2 >= 0)
        val geoFences = db.geoFenceDao().getActiveGeoFences()
        assert(geoFences.isNotEmpty())
        assert(geoFences[0].active && (geoFences[0].name == tmpGeoFence.name))
    }

    @Test
    fun getAnyGeoFence() {
        assert(db.geoFenceDao().getAnyGeoFence() == null)

        val tmpGeoFence = GeoFence(randomUuid(), true, randomUuid(), randomDouble(), randomDouble(), randomDouble())
        val entityId = db.geoFenceDao().insert(tmpGeoFence)
        assert(entityId >= 0)
        assert(db.geoFenceDao().getAnyGeoFence() != null)
    }

    @Test
    fun insertGeoFence() {
        val tmpGeoFence = GeoFence(randomUuid(), true, randomUuid(), randomDouble(), randomDouble(), randomDouble())
        val entityId = db.geoFenceDao().insert(tmpGeoFence)

        assert(entityId >= 0)
        val geoFenceList = db.geoFenceDao().getGeoFences()
        geoFenceList.observe(
            this,
            Observer<List<GeoFence>> {
                assert(it!!.isNotEmpty())
            }
        )

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

    fun randomDouble(): Double {
        return randomInt().toDouble()
    }
}