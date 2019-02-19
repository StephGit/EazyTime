package ch.bfh.mad.eazytime.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import ch.bfh.mad.eazytime.data.entity.Project

@Dao
interface ProjectDao {
    @Query("SELECT * FROM project")
    fun getProjects(): List<Project>

    @Query("SELECT * FROM project")
    fun getProjectsLiveData(): LiveData<List<Project>>

    @Insert
    fun insertAll(projects: List<Project>)

    @Insert
    fun insert(project: Project)

    @Update
    fun update(project: Project)

    @Delete
    fun delete(project: Project)

    @Query("SELECT * FROM project WHERE isDefault = 1")
    fun getDefaultProject(): Project
}