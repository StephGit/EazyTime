package ch.bfh.mad.eazytime.projects

import ch.bfh.mad.eazytime.data.entity.Project

class FakeProjectRepo() {

    private lateinit var projects: MutableList<Project>

    init {
        projects = getProjectsFromDB()
    }

    fun getProjects(): List<Project>{
        return projects
    }

    fun addProject(name: String, shortCode: String, color: String, onWidget: Boolean? = false, default: Boolean? = false){
        val newProject = Project((projects.size+1).toLong(), name, shortCode,color, onWidget, default)
        projects.add(newProject)
    }

    private fun getProjectsFromDB(): MutableList<Project> {
        return arrayListOf(
            Project(1, "DeX", "DeX", "a9e34b", true, true),
            Project(2, "Admin", "ADM", "f783ac", true, false),
            Project(3, "HassProjekt", ":-(", "3bc9db", true, false),
            Project(4, "Ramsch", "RMS", "9775fa", true, false),
            Project(5, "Umelungere", "ULG", "ffa94d", true, false)
        )
    }
}