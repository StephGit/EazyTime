package ch.bfh.mad

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import ch.bfh.mad.eazytime.AppDatabase
import ch.bfh.mad.eazytime.entity.Project
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