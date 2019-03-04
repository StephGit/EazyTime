package ch.bfh.mad.eazytime.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ch.bfh.mad.eazytime.data.dao.GeoFenceDao
import ch.bfh.mad.eazytime.data.dao.ProjectDao
import ch.bfh.mad.eazytime.data.dao.TimeSlotDao
import ch.bfh.mad.eazytime.data.dao.WorkDayDao
import ch.bfh.mad.eazytime.data.entity.GeoFence
import ch.bfh.mad.eazytime.data.entity.Project
import ch.bfh.mad.eazytime.data.entity.TimeSlot
import ch.bfh.mad.eazytime.data.entity.WorkDay

@Database(entities = [Project::class, TimeSlot::class, GeoFence::class, WorkDay::class], version = 1)
@TypeConverters(DatabaseTypeConverters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun timeSlotDao(): TimeSlotDao
    abstract fun geoFenceDao(): GeoFenceDao
    abstract fun workDayDao(): WorkDayDao
}