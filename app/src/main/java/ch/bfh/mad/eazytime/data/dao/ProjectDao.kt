package ch.bfh.mad.eazytime.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ch.bfh.mad.eazytime.data.entity.Project

@Dao
interface ProjectDao {
    @Query("SELECT * FROM project WHERE isDeleted == 0")
    fun getProjects(): List<Project>

    @Query("SELECT * FROM project WHERE isDeleted == 0")
    fun getProjectsLiveData(): LiveData<List<Project>>

    @Query("SELECT * FROM project")
    fun getAllProjects(): List<Project>

    @Query("SELECT * FROM project")
    fun getAllProjectsLiveData(): LiveData<List<Project>>

    @Insert
    fun insertAll(projects: List<Project>)

    @Insert
    fun insert(project: Project)

    @Update
    fun update(project: Project)

    @Update
    fun updateAll(projects: List<Project>)

    @Query("SELECT * FROM project WHERE isDefault = 1")
    fun getDefaultProject(): Project

    @Query("SELECT * FROM project WHERE id == :projectId")
    fun getProjectById(projectId: Long): Project?

    @Query("SELECT * FROM project WHERE onWidget = 1")
    fun geProjectsForWidget(): List<Project>
}