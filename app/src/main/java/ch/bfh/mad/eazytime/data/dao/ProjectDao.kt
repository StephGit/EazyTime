package ch.bfh.mad.eazytime.data.dao

import android.arch.persistence.room.*
import ch.bfh.mad.eazytime.data.entity.Project

@Dao
interface ProjectDao {
    @Query("SELECT * FROM project")
    fun getProjects(): List<Project>

    @Insert
    fun insertAll(projects: List<Project>)

    @Update
    fun update(project: Project)

    @Delete
    fun delete(project: Project)
}