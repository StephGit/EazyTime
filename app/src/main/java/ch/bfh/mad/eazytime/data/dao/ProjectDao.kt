package ch.bfh.mad.eazytime.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
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

    @Update
    fun updateAll(projects: List<Project>)

    @Delete
    fun delete(project: Project)

    @Query("SELECT * FROM project WHERE isDefault = 1")
    fun getDefaultProject(): Project

    @Query("SELECT * FROM project WHERE id == :projectId")
    fun getProjectById(projectId: Long): Project?

    @Query("DELETE FROM project WHERE id == :projectId")
    fun deleteProjectById(projectId: Long)

    @Query("SELECT * FROM project WHERE onWidget = 1")
    fun geProjectsForWidget(): List<Project>
}