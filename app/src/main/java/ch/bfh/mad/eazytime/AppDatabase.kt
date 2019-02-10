package ch.bfh.mad.eazytime

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import ch.bfh.mad.eazytime.dao.GeoFenceDao
import ch.bfh.mad.eazytime.dao.ProjectDao
import ch.bfh.mad.eazytime.dao.TimeSlotDao
import ch.bfh.mad.eazytime.dao.WorkDayDao
import ch.bfh.mad.eazytime.entity.GeoFence
import ch.bfh.mad.eazytime.entity.Project
import ch.bfh.mad.eazytime.entity.TimeSlot
import ch.bfh.mad.eazytime.entity.WorkDay

@Database(entities = [Project::class, TimeSlot::class, GeoFence::class, WorkDay::class], version = 1)
@TypeConverters(DatabaseTypeConverters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun timeSlotDao(): TimeSlotDao
    abstract fun geoFenceDao(): GeoFenceDao
    abstract fun workDayDao(): WorkDayDao
}